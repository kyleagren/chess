package websocket.messages;

import chess.ChessGame;

public class Notification extends ServerMessage {
    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        setMessageText(message);
    }
}
