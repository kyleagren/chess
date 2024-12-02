package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;
    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.setMessageText(errorMessage);
    }

    @Override
    public void setMessageText(String text) {
        errorMessage = text;
    }

    @Override
    public String getMessageText() {
        return errorMessage;
    }
}
