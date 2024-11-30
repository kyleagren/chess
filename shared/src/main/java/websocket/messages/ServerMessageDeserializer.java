package websocket.messages;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {

    @Override
    public ServerMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String messageType = jsonObject.get("serverMessageType").getAsString();

        return switch (messageType) {
            case "NOTIFICATION" -> context.deserialize(jsonObject, Notification.class);
            case "LOAD_GAME" -> context.deserialize(jsonObject, LoadGameMessage.class);
            case "ERROR" -> context.deserialize(jsonObject, ErrorMessage.class);
            default -> throw new JsonParseException("Unknown message type: " + messageType);
        };
    }
}
