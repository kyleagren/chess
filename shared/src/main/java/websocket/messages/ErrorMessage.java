package websocket.messages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage {
    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        setMessageText(message);
    }
}
