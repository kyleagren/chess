package websocket.commands;

import com.google.gson.*;
import websocket.messages.ServerMessage;

import java.lang.reflect.Type;

public class UserGameCommandDeserializer implements JsonDeserializer<ServerMessage> {

    @Override
    public ServerMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String messageType = jsonObject.get("commandType").getAsString();

        return switch (messageType) {
            case "CONNECT", "LEAVE", "RESIGN" -> context.deserialize(jsonObject, UserGameCommand.class);
            case "MAKE_MOVE" -> context.deserialize(jsonObject, MakeMoveCommand.class);
            default -> throw new JsonParseException("Unknown message type: " + messageType);
        };
    }
}