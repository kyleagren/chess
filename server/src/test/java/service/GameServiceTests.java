package service;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.EmptySuccessResponse;
import server.GameCreatedResponse;
import server.GamesListResponse;
import server.TruncatedGameData;

import java.util.ArrayList;

public class GameServiceTests {
    private static GameService gameService;
    private static AuthService authService;

    private static AuthData authResult;

    @BeforeAll
    public static void init() {
        gameService = new GameService();
        authService = new AuthService();
    }

    @BeforeEach
    public void setup() {
        gameService.deleteAll();
        authService.deleteAll();
        try {
            authResult = authService.createAuth("username");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    @Test
    public void createGameSuccess() {
        Object gameResult = null;
        try {
            gameResult = gameService.createGame(authResult.authToken(), "game1");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNotNull(gameResult);
        Assertions.assertEquals(GameCreatedResponse.class, gameResult.getClass());
    }

    @Test
    public void createGameFailure() {
        // invalid token will not allow game to be created.
        Assertions.assertThrows(Exception.class, () -> gameService.createGame("invalid", "game1"));
    }

    @Test
    public void joinGameSuccess() {
        Object gameResult = null;
        Object joinResult = null;
        int gameID = 0;
        try {
            gameResult = gameService.createGame(authResult.authToken(), "game1");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        if (gameResult.getClass() == GameCreatedResponse.class) {
            gameID = ((GameCreatedResponse) gameResult).gameID();
        }
        try {
            joinResult = gameService.joinGame(authResult.authToken(), gameID, "WHITE");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertEquals(EmptySuccessResponse.class, joinResult.getClass());
    }

    @Test
    public void joinGameFailure() {
        Object gameResult = null;
        int gameID;
        try {
            gameResult = gameService.createGame(authResult.authToken(), "game1");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        if (gameResult.getClass() == GameCreatedResponse.class) {
            gameID = ((GameCreatedResponse) gameResult).gameID();
        } else {
            gameID = 2;
        }
        // gameID 0 should not exist so it should fail.
        Assertions.assertThrows(Exception.class, () -> gameService.joinGame(authResult.authToken(),
                0, "WHITE"));
    }

    @Test
    public void getGamesSuccess() {
        Object gameList = null;
        try {
            gameService.createGame(authResult.authToken(), "game1");
            gameService.createGame(authResult.authToken(), "game2");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        try {
            gameList = gameService.getGames(authResult.authToken());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertEquals(GamesListResponse.class, gameList.getClass());
    }

    @Test
    public void getGamesFailure() {
        try {
            gameService.createGame(authResult.authToken(), "game1");
            gameService.createGame(authResult.authToken(), "game2");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertThrows(Exception.class, () -> gameService.getGames("invalid"));
    }

    @Test
    public void deleteAllSuccess() {
        Object gameList = null;
        try {
            gameService.createGame(authResult.authToken(), "game1");
            gameService.createGame(authResult.authToken(), "game2");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        try {
            gameService.deleteAll();
            gameList = gameService.getGames(authResult.authToken());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        ArrayList<TruncatedGameData> response = ((GamesListResponse) gameList).listGames();
        Assertions.assertEquals(0, response.size());
    }
}
