package dop54321.com.androidfinalex;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class GameRecord {
    private int gameId;
    private List<GameCard> gameCards;


    public GameRecord() {
        gameId=-1;
        gameCards =new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            gameCards.add(i,new GameCard());
        }
    }

    public GameRecord(int gameId, List<GameCard> imageUris) {
        this.gameId = gameId;
        this.gameCards = imageUris;
    }


    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public List<GameCard> getGameCards() {
        return gameCards;
    }

    public void setGameCards(List<GameCard> gameCards) {
        this.gameCards = gameCards;
    }
}
