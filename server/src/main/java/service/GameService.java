package service;

import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.GameDataAccessMemory;
import model.AuthData;
import server.EmptySuccessResponse;
import server.GameCreatedResponse;
import server.GamesListResponse;

public class GameService {
    private final GameDataAccess gameDataAccess = new GameDataAccessMemory();
    private final AuthService authService = new AuthService();


    public Object createGame(String token, String gameName) throws Exception {
        try {
            if (authService.getAuth(token) != null) {
                int gameID = gameDataAccess.createGame(gameName);
                return new GameCreatedResponse(gameID);
            }
            else {
                throw new Exception("not found");
            }
        } catch (DataAccessException e) {
            throw new Exception(e.getMessage());
        }
    }

    public Object joinGame(String token, int gameID, String playerColor) throws Exception {
        try {
            AuthData auth = authService.getAuth(token);
            if (auth != null) {
                try {
                    if (gameDataAccess.checkAvailability(gameID, playerColor)) {

                        gameDataAccess.joinGame(gameID, playerColor, auth.username());
                    }
                    else {
                        throw new Exception("already taken");
                    }
                } catch (DataAccessException e) {
                    throw new Exception(e.getMessage());
                }
                return new EmptySuccessResponse();
            }
            else {
                throw new Exception("not found");
            }
        } catch (DataAccessException e) {
            throw new Exception(e.getMessage());
        }
    }

    public Object getGames(String token) throws Exception {
        try {
            if (authService.getAuth(token) != null) {
                return new GamesListResponse(gameDataAccess.getGames());
            }
            else {
                throw new Exception("not found");
            }
        } catch (DataAccessException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteAll() {
        gameDataAccess.deleteAll();
    }
}
