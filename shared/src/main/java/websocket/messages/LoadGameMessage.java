package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGameMessage extends ServerMessage {
    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        setMessageText(new Gson().toJson(game, ChessGame.class));
    }
}
