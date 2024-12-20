package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.GameDataAccessMySQL;
import model.AuthData;
import model.GameData;
import response.EmptySuccessResponse;
import response.GameCreatedResponse;
import response.GamesListResponse;

public class GameService {
    private final GameDataAccess gameDataAccess = new GameDataAccessMySQL();
    private final AuthService authService = new AuthService();


    public GameCreatedResponse createGame(String token, String gameName) throws Exception {
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

    public EmptySuccessResponse joinGame(String token, int gameID, String playerColor) throws Exception {
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

    public GamesListResponse getGames(String token) throws Exception {
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
        try {
            gameDataAccess.deleteAll();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return gameDataAccess.getGame(gameID);
    }

    public void updateGame(int gameID, GameData game) throws DataAccessException {
        gameDataAccess.updateGame(gameID, game);
    }

    public ChessGame getGameState(int gameID) throws DataAccessException {
        return gameDataAccess.getGameState(gameID);
    }
}
