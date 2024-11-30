package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService = new AuthService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String username = getUsername(command.getAuthToken());
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(username, session);
            case UserGameCommand.CommandType.LEAVE -> leave(username);
        }
    }

    private void connect(String username, Session session) throws IOException {
        connections.add(username, session);
        var message = String.format("%s has joined the game.", username);
        var notification = new Notification(message);
        connections.broadcast(username, notification);
    }

    private void leave(String username) throws IOException {
        connections.remove(username);
        var message = String.format("%s has left the game.", username);
        var notification = new Notification(message);
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
