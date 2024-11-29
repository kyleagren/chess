package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String username = getUsername(command.getAuthToken());
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(username, command.getColor(), session);
            case UserGameCommand.CommandType.LEAVE -> leave(username);
        }
    }

    private void connect(String username, String color, Session session) throws IOException {
        connections.add(username, session);
        var message = String.format("%s has joined the game as the %s player.", username, color);
        var notification = new Notification(message);
        connections.broadcast(username, notification);
    }

    private void leave(String username) throws IOException {
        connections.remove(username);
        var message = String.format("%s has left the game.", username);
        var notification = new Notification(message);
        connections.broadcast(username, notification);
    }
}
