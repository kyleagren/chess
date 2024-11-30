package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

public class LoadGameMessage extends ServerMessage {
    GameData game;
    public LoadGameMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }
}
