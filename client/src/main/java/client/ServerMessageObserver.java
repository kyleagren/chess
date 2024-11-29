package client;

import websocket.messages.ServerMessage;

public interface ServerMessageObserver {
    void notifyWS(ServerMessage message);
}
