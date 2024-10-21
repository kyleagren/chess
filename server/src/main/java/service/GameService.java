package service;

import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.GameDataAccessMemory;
import server.GameCreatedResponse;

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
}
