package client;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import response.GameCreatedResponse;
import response.JoinGameRequestBody;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
    }

    @BeforeEach
    public void clearDB() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerSuccess() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        Assertions.assertDoesNotThrow(() -> facade.register(data));
    }

    @Test
    public void registerFailure() throws ResponseException {
        UserData data = new UserData(null, null, null);
        Assertions.assertThrows(Exception.class, () -> facade.register(data));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData result = facade.register(data);
        facade.logout(result.authToken());
        Assertions.assertDoesNotThrow(() -> facade.login(data));
    }

    @Test
    public void loginFailure() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData result = facade.register(data);
        facade.logout(result.authToken());
        Assertions.assertThrows(Exception.class, () -> facade.login(new UserData(null,
                null, null)));
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData result = facade.register(data);
        Assertions.assertDoesNotThrow(() -> facade.logout(result.authToken()));
    }

    @Test
    public void logoutFailure() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData result = facade.register(data);
        Assertions.assertThrows(Exception.class, () -> facade.logout(null));
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData result = facade.register(data);

        GameData gameData = new GameData(1, null, null,
                "test", new ChessGame());
        Assertions.assertDoesNotThrow(() -> facade.createGame(gameData, result.authToken()));
    }

    @Test
    public void createGameFailure() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData result = facade.register(data);

        GameData gameData = new GameData(1, null, null,
                null, new ChessGame());
        Assertions.assertThrows(Exception.class, () -> facade.createGame(gameData, result.authToken()));
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData authResult = facade.register(data);

        GameData gameData = new GameData(1, null, null,
                "test", new ChessGame());
        GameCreatedResponse res = facade.createGame(gameData, authResult.authToken());
        JoinGameRequestBody req = new JoinGameRequestBody("WHITE", res.gameID());
        Assertions.assertDoesNotThrow(() -> facade.joinGame(req, authResult.authToken()));
    }

    @Test
    public void joinGameFailure() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData authResult = facade.register(data);

        GameData gameData = new GameData(1, null, null,
                "test", new ChessGame());
        GameCreatedResponse res = facade.createGame(gameData, authResult.authToken());
        JoinGameRequestBody req = new JoinGameRequestBody(null, res.gameID());
        Assertions.assertThrows(Exception.class, () -> facade.joinGame(req, authResult.authToken()));
    }

    @Test
    public void listGamesSuccess() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData authResult = facade.register(data);

        GameData gameData = new GameData(1, null, null,
                "test", new ChessGame());
        GameCreatedResponse res = facade.createGame(gameData, authResult.authToken());
        Assertions.assertDoesNotThrow(() -> facade.listGames(authResult.authToken()));
    }

    @Test
    public void listGamesFailure() throws ResponseException {
        UserData data = new UserData("test", "test", "test");
        AuthData authResult = facade.register(data);

        GameData gameData = new GameData(1, null, null,
                "test", new ChessGame());
        GameCreatedResponse res = facade.createGame(gameData, authResult.authToken());
        Assertions.assertThrows(Exception.class, () -> facade.listGames(null));
    }
}
