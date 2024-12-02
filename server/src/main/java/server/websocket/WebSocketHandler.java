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
            DataAccessException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(UserGameCommand.class, new UserGameCommandDeserializer())
                .create();
        var command = gson.fromJson(message, UserGameCommand.class);

        String username = getUsername(command.getAuthToken());
        if (username == null) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid auth token.")));
            return;
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

    private void connect(String username, Session session, int gameID) throws IOException {
        GameData game;
        try {
            game = gameService.getGame(gameID);
        } catch (DataAccessException ex) {
            connections.add(username, session, gameID);
            connections.send(username, new Gson().toJson(new ErrorMessage("Invalid game number")));
            return;
        }
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
        connections.add(username, session, gameID);
        var notification = new Gson().toJson(new Notification(message));
        var loadNotification = new Gson().toJson(new LoadGameMessage(game));
        connections.send(username, loadNotification);
        connections.broadcast(username, notification);
    }

    private void leave(String username, int gameID) throws IOException, DataAccessException {
        GameData game = gameService.getGame(gameID);
        GameData newGame = null;
        if (username.equals(game.whiteUsername())) {
            newGame = new GameData(gameID, null, game.blackUsername(), game.gameName(), game.game());
        }
        if (username.equals(game.blackUsername())) {
            newGame = new GameData(gameID, game.whiteUsername(), null, game.gameName(), game.game());
        }
        if (newGame != null) {
            // leaves the game in the database if the user isn't an observer
            gameService.updateGame(gameID, newGame);
        }
        var message = String.format("%s has left the game.", username);
        var notification = new Gson().toJson(new Notification(message));
        connections.broadcast(username, notification);
        connections.remove(username);
    }

    private void resign(String username, int gameID) throws IOException, DataAccessException {
        GameData game = gameService.getGame(gameID);
        GameData newGame = null;
        if (game.game().getGameOver()) {
            connections.send(username, new Gson().toJson(new ErrorMessage("Error: The game is already over.")));
            return;
        }
        if (username.equals(game.whiteUsername())) {
            newGame = checkGameOver(game, gameID);
        }
        else if (username.equals(game.blackUsername())) {
            newGame = checkGameOver(game, gameID);
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
        connections.sendToAll(username, notification);
    }

    private GameData checkGameOver(GameData game, int gameID) {
        ChessGame chessGame = game.game();
        chessGame.setGameOver(true);
        return new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
    }

    private void makeMove(String username, ChessMove move, int gameID)
            throws DataAccessException, IOException {
        GameData game = gameService.getGame(gameID);
        GameData newGame = null;
        if (game.game().getGameOver()) {
            connections.send(username, new Gson().toJson(new ErrorMessage("Error: The game is over.")));
            return;
        }
        if (username.equals(game.whiteUsername())) {
            newGame = checkMoveValidity(username, game, ChessGame.TeamColor.WHITE, move);
        }
        else if (username.equals(game.blackUsername())) {
            newGame = checkMoveValidity(username, game, ChessGame.TeamColor.BLACK, move);
        }
        else {
            // Observers shouldn't be able to make moves
            connections.send(username, new Gson().toJson(new ErrorMessage("Error: Observers can't make moves.")));
            return;
        }
        if (newGame == null) {
            return;
        }
        gameService.updateGame(gameID, newGame);
        var message = String.format("%s has moved from %s to %s.", username,
                move.getStartPosition(), move.getEndPosition());
        var notification = new Gson().toJson(new Notification(message));
        var loadNotification = new Gson().toJson(new LoadGameMessage(newGame));
        connections.sendToAll(username, loadNotification);
        connections.broadcast(username, notification);

        String blackUser = newGame.blackUsername();
        String whiteUser = newGame.whiteUsername();
        if (newGame.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            message = String.format("%s (black) is in checkmate.", blackUser);
        }
        else if (newGame.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            message = String.format("%s (white) is in checkmate.", whiteUser);
        }
        else if (newGame.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
            message = String.format("%s (black) is in stalemate.", blackUser);
        }
        else if (newGame.game().isInStalemate(ChessGame.TeamColor.WHITE)) {
            message = String.format("%s (white) is in stalemate.", whiteUser);
        }
        else if (newGame.game().isInCheck(ChessGame.TeamColor.BLACK)) {
            message = String.format("%s (black) is in check.", blackUser);
        }
        else if (newGame.game().isInCheck(ChessGame.TeamColor.WHITE)) {
            message = String.format("%s (white) is in check.", whiteUser);
        }
        else {
            return;
        }
        notification = new Gson().toJson(new Notification(message));
        connections.sendToAll(username, notification);
    }

    private String getUsername(String token) throws DataAccessException {
        AuthData authData = authService.getAuth(token);
        if (authData != null) {
            return authData.username();
        }
        return null;
    }

    private GameData checkMoveValidity(String username, GameData game,
                                       ChessGame.TeamColor color, ChessMove move) throws IOException {
        ChessGame chessGame = game.game();
        if (chessGame.getTeamTurn() != color) {
            connections.send(username, new Gson().toJson(new ErrorMessage("Error: It's not your turn")));
            return null;
        }
        var validMoves = chessGame.validMoves(move.getStartPosition());
        if (validMoves == null) {
            connections.send(username, new Gson().toJson(new ErrorMessage("Error: Invalid move")));
            return null;
        }
        else if (validMoves.contains(move)) {
            try {
                chessGame.makeMove(move);
            } catch (InvalidMoveException ex) {
                connections.send(username, new Gson().toJson(new ErrorMessage("Error: Invalid move")));
                return null;
            }
        }
        else {
            connections.send(username, new Gson().toJson(new ErrorMessage("Error: Invalid move")));
            return null;
        }
        return new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
    }
}
