package dop54321.com.androidfinalex;

import android.net.Uri;

import java.util.Collections;
import java.util.List;

/**
 * Created by dop54321 on 17/07/2015.
 */
public class MemoryGame {
    private List<GameCard> mGameCards;
    private int mGuessCounter;
    private int mTrueGuesses;

    public MemoryGame(List<GameCard> gameCards) {
        mGameCards = gameCards;
        mGuessCounter = 0;
        mTrueGuesses = 0;
    }


    public List<GameCard> shuffleCards() {
        Collections.shuffle(mGameCards);
        return mGameCards;
    }

    public boolean guess(int firstCardPosition, int secondCardPosition) {
        Boolean success = false;
        Uri firstCardImg = mGameCards.get(secondCardPosition).getImageRef();
        Uri secondCardImg = mGameCards.get(firstCardPosition).getImageRef();
        if (secondCardImg.equals(firstCardImg)) {
            success = true;
            mTrueGuesses++;
        }
        this.mGuessCounter++;
        return success;
    }

    public Boolean isGameOver(){
        Boolean res=false;

        if (mTrueGuesses==this.mGameCards.size()/2){
            res=true;
        }
        return res;
    }

    public int getmGuessCounter() {
        return mGuessCounter;
    }

    public List<GameCard> getmGameCards() {
        return mGameCards;
    }

    public void startNewGame(){
        mGuessCounter = 0;
        mTrueGuesses = 0;
    }
}
