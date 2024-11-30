package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommandDeserializer;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageDeserializer;

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
            case UserGameCommand.CommandType.CONNECT -> connect(username, session);
            case UserGameCommand.CommandType.LEAVE -> leave(username, command.getGameID());
            case UserGameCommand.CommandType.RESIGN -> resign(username,command.getGameID());
            case UserGameCommand.CommandType.MAKE_MOVE -> {
                MakeMoveCommand newCommand = (MakeMoveCommand) command;
                makeMove(username, newCommand.getMove(), command.getGameID());
            }
        }
    }

    private void connect(String username, Session session) throws IOException {
        connections.add(username, session);
        var message = String.format("%s has joined the game.", username);
        var notification = new Gson().toJson(new Notification(message));
        connections.broadcast(username, notification);
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
            return;
        }

        gameService.updateGame(gameID, newGame);
        var message = String.format("%s has resigned the game.", username);
        var notification = new Gson().toJson(new Notification(message));
        connections.broadcast(username, notification);
    }

    private void makeMove(String username, ChessMove move, int gameID) throws DataAccessException, InvalidMoveException, IOException {
        GameData game = gameService.getGame(gameID);
        GameData newGame = null;
        if (username.equals(game.whiteUsername())) {
            ChessGame chessGame = game.game();
            if (chessGame.getTeamTurn() != ChessGame.TeamColor.WHITE) {
                return;
            }
            var validMoves = chessGame.validMoves(move.getStartPosition());
            if (validMoves.contains(move)) {
                chessGame.makeMove(move);
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
            newGame = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
        }
        else {
            // Observers shouldn't be able to make moves
            return;
        }
        gameService.updateGame(gameID, newGame);
        var message = String.format("%s has moved from %s to %s.", username,
                move.getStartPosition(), move.getEndPosition());
        var notification = new Gson().toJson(new Notification(message));
        var loadNotification = new Gson().toJson(new LoadGameMessage(newGame));
        connections.sendToAll(loadNotification);
        connections.broadcast(username, notification);
    }

    private String getUsername(String token) throws DataAccessException {
        AuthData authData = authService.getAuth(token);
        if (authData != null) {
            return authData.username();
        }
        return null;
    }
}