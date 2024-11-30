package client;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import response.*;

public class ServerFacade {
    private final String serverUrl;
    private ClientCommunicator communicator;

    public ServerFacade(String url) {
        serverUrl = url;
        communicator = new ClientCommunicator(url);
    }

    public AuthData register(UserData data) throws ResponseException {
        var path = "/user";
        return communicator.makeRequest("POST", path, data, AuthData.class, "");
    }

    public AuthData login(UserData data) throws ResponseException {
        var path = "/session";
        return communicator.makeRequest("POST", path, data, AuthData.class, "");
    }

    public EmptySuccessResponse logout(String token) throws ResponseException {
        var path = "/session";
        return communicator.makeRequest("DELETE", path, null, EmptySuccessResponse.class, token);
    }

    public GameCreatedResponse createGame(GameData data, String token) throws ResponseException {
        var path = "/game";
        return communicator.makeRequest("POST", path, data, GameCreatedResponse.class, token);
    }

    public EmptySuccessResponse joinGame(JoinGameRequestBody info, String token) throws ResponseException {
        var path = "/game";
        return communicator.makeRequest("PUT", path, info, EmptySuccessResponse.class, token);
    }

    public GamesListResponse listGames(String token) throws ResponseException {
        var path = "/game";
        return communicator.makeRequest("GET", path, null, GamesListResponse.class, token);
    }

    public EmptySuccessResponse clear() throws ResponseException {
        var path = "/db";
        return communicator.makeRequest("DELETE", path, null, EmptySuccessResponse.class, "");
    }

    public GameData getGame(GetGameRequestBody data, String token) throws ResponseException {
        var path = "/games";
        return communicator.makeRequest("PUT", path, data, GameData.class, token);
    }
}
