package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommandDeserializer;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService = new AuthService();
    private final GameService gameService = new GameService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException,
            DataAccessException, InvalidMoveException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(UserGameCommand.class, new UserGameCommandDeserializer())
                .create();
        var command = gson.fromJson(message, UserGameCommand.class);

        String username = getUsername(command.getAuthToken());
        if (username == null) {
            throw new DataAccessException("user not found");
        }
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(username, session, command.getGameID());
            case UserGameCommand.CommandType.LEAVE -> leave(username, command.getGameID());
            case UserGameCommand.CommandType.RESIGN -> resign(username, command.getGameID());
            case UserGameCommand.CommandType.MAKE_MOVE -> {
                MakeMoveCommand newCommand = (MakeMoveCommand) command;
                makeMove(username, newCommand.getMove(), command.getGameID());
            }
        }
    }

    private void connect(String username, Session session, int gameID) throws IOException, DataAccessException {
        GameData game = gameService.getGame(gameID);
        String message;
        if (username.equals(game.whiteUsername())) {
            message = String.format("%s has joined the game as the white player.", username);
        }
        else if (username.equals(game.blackUsername())) {
            message = String.format("%s has joined the game as the black player.", username);
        }
        else {
            message = String.format("%s has joined the game as an observer.", username);
        }
        connections.add(username, session);
        var notification = new Gson().toJson(new Notification(message));
        var loadNotification = new Gson().toJson(new LoadGameMessage(game));
        connections.send(username, loadNotification);
        connections.sendToAll(notification);
    }

    private void leave(String username, int gameID) throws IOException, DataAccessException {
        GameData game = gameService.getGame(gameID);
        GameData newGame = null;
        if (username.equals(game.whiteUsername())) {
            newGame = new GameData(gameID, null, game.blackUsername(), game.gameName(), game.game());
        }
        if (username.equals(game.blackUsername())) {
            newGame = new GameData(gameID, null, game.blackUsername(), game.gameName(), game.game());
        }
        if (newGame != null) {
            // leaves the game in the database if the user isn't an observer
            gameService.updateGame(gameID, newGame);
        }
        connections.remove(username);
        var message = String.format("%s has left the game.", username);
        var notification = new Gson().toJson(new Notification(message));
        connections.broadcast(username, notification);
    }

    private void resign(String username, int gameID) throws IOException, DataAccessException {
        GameData game = gameService.getGame(gameID);
        GameData newGame = null;
        if (username.equals(game.whiteUsername())) {
            ChessGame chessGame = game.game();
            chessGame.setGameOver(true);
            newGame = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
        }
        else if (username.equals(game.blackUsername())) {
            ChessGame chessGame = game.game();
            chessGame.setGameOver(true);
            newGame = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
        }
        else {
            // observer shouldn't be able to resign
            var message = "You can't resign as an observer.";
            var error = new Gson().toJson(new ErrorMessage(message));
            connections.send(username, error);
            return;
        }

        gameService.updateGame(gameID, newGame);
        var message = String.format("%s has resigned the game.", username);
        var notification = new Gson().toJson(new Notification(message));
        connections.broadcast(username, notification);
    }

    private void makeMove(String username, ChessMove move, int gameID)
            throws DataAccessException, InvalidMoveException, IOException {
        GameData game = gameService.getGame(gameID);
        GameData newGame = null;
        if (username.equals(game.whiteUsername())) {
            ChessGame chessGame = game.game();
            if (chessGame.getTeamTurn() != ChessGame.TeamColor.WHITE) {
                connections.send(username, new Gson().toJson(new ErrorMessage("Error: It's not your turn")));
            }
            var validMoves = chessGame.validMoves(move.getStartPosition());
            if (validMoves.contains(move)) {
                chessGame.makeMove(move);
            }
            else {
                connections.send(username, new Gson().toJson(new ErrorMessage("Error: Invalid move")));
                return;
            }
            newGame = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
        }
        else if (username.equals(game.blackUsername())) {
            ChessGame chessGame = game.game();
            if (chessGame.getTeamTurn() != ChessGame.TeamColor.BLACK) {
                return;
            }
            var validMoves = chessGame.validMoves(move.getStartPosition());
            if (validMoves.contains(move)) {
                chessGame.makeMove(move);
            }
            else {
                connections.send(username, new Gson().toJson(new ErrorMessage("Error: Invalid move")));
                return;
            }
            newGame = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
        }
        else {
            // Observers shouldn't be able to make moves
            connections.send(username, new Gson().toJson(new ErrorMessage("Error: Observers can't make moves.")));
            return;
        }
        gameService.updateGame(gameID, newGame);
        var message = String.format("%s has moved from %s to %s.", username,
                move.getStartPosition(), move.getEndPosition());
        var notification = new Gson().toJson(new Notification(message));
        var loadNotification = new Gson().toJson(new LoadGameMessage(newGame));
        connections.sendToAll(loadNotification);
        connections.broadcast(username, notification);

        if (newGame.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            message = "Black is in checkmate.";
        }
        else if (newGame.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            message = "White is in checkmate.";
        }
        else if (newGame.game().isInCheck(ChessGame.TeamColor.BLACK)) {
            message = "Black is in check.";
        }
        else if (newGame.game().isInCheck(ChessGame.TeamColor.WHITE)) {
            message = "White is in check.";
        }
        else if (newGame.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
            message = "Black is in stalemate.";
        }
        else if (newGame.game().isInStalemate(ChessGame.TeamColor.WHITE)) {
            message = "White is in stalemate";
        }
        else {
            return;
        }
        notification = new Gson().toJson(new Notification(message));
        connections.sendToAll(notification);
    }

    private String getUsername(String token) throws DataAccessException {
        AuthData authData = authService.getAuth(token);
        if (authData != null) {
            return authData.username();
        }
        return null;
    }
}
