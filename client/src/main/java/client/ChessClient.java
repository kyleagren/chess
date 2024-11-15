package client;

public abstract class ChessClient {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String newToken) {
        token = newToken;
    }

    abstract public String eval(String input);
    abstract public String help();
}
