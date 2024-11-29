package client;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
    Session session;
    ServerMessageObserver observer;

    public WebSocketCommunicator(String url, ServerMessageObserver observer) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url+"/ws");
            this.observer = observer;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String incomingMessage) {
                    try {
                        ServerMessage message = new Gson().fromJson(incomingMessage, ServerMessage.class);
                        observer.notify(message);
                    } catch(Exception ex) {
                        observer.notify(new ErrorMessage(ex.getMessage()));
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void joinGame(String username, String color) throws ResponseException {
        try {
            this.session.getBasicRemote().sendText(username + " has joined the game as " + color);
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leaveGame(String username) throws ResponseException {
        try {
            this.session.getBasicRemote().sendText(username + " has left the game.");
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
