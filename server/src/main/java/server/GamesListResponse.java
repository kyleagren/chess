package server;

import model.GameData;

import java.util.ArrayList;

public class GamesListResponse {
    ArrayList<TruncatedGameData> games = new ArrayList<>();

    public GamesListResponse(ArrayList<GameData> currentGames) {
        for (GameData currentGame : currentGames) {
            TruncatedGameData newData = new TruncatedGameData(currentGame.gameID(),
                    currentGame.whiteUsername(), currentGame.blackUsername(), currentGame.gameName());
            games.add(newData);
        }
    }
    public  ArrayList<TruncatedGameData> listGames() {
        return games;
    }
}
