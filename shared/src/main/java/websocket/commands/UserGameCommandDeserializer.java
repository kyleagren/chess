package websocket.commands;

import com.google.gson.*;

import java.lang.reflect.Type;

public class UserGameCommandDeserializer implements JsonDeserializer<UserGameCommand> {

    @Override
    public UserGameCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String messageType = jsonObject.get("commandType").getAsString();

        return switch (messageType) {
            case "CONNECT" -> context.deserialize(jsonObject, ConnectCommand.class);
            case "LEAVE" -> context.deserialize(jsonObject, LeaveCommand.class);
            case "RESIGN" -> context.deserialize(jsonObject, ResignCommand.class);
            case "MAKE_MOVE" -> context.deserialize(jsonObject, MakeMoveCommand.class);
            default -> throw new JsonParseException("Unknown message type: " + messageType);
        };
    }
}