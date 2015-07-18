package dop54321.com.androidfinalex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity implements GridRecyclerViewAdapter.OnItemClickCallback {

    public static final String GAME_ID_EXTRA = "gameIdExtra";
    private RecyclerView mRecyclerView;
    private MemoryGame mMemoryGame;
    private GridRecyclerViewAdapter mAdapter;
    private int mFlipCounter = 0;
    private int mFirstCardPosition = -1;
    private int mSecondCardPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get game id to be loaded from intent data, and load the game record from the sqlite database
        Intent intent = getIntent();
        if (intent != null) {
            int gameId = intent.getIntExtra(GAME_ID_EXTRA, -1);
            if (gameId == -1) {
                MakeMessage.show(this, "Game not found...");
                this.finish();
            } else {
                MySqlAdapter sqlManager = MySqlAdapter.getInstanse();
                if (sqlManager.gameAlreadyExist(gameId)) {
                    GameRecord gameRecord = sqlManager.getGameRecord(gameId);
                    this.mMemoryGame = new MemoryGame(this.makeGameCards(gameRecord));
                }
            }
        } else {
            finish();
        }

        //initialize the recyclerview withe the adapter and the cards list
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 4);

        mRecyclerView.setLayoutManager(mLayoutManager);

        initGameCardsToBackAndShuffle();

        mAdapter = new GridRecyclerViewAdapter(this.mMemoryGame.getmGameCards(), this);
        mAdapter.setMyCallback(this);

        mRecyclerView.setAdapter(mAdapter);


    }

    private void initGameCardsToBackAndShuffle() {

        for (GameCard gameCard : this.mMemoryGame.getmGameCards()) {
            gameCard.setCardBackSided();
        }
        mMemoryGame.shuffleCards();
        mMemoryGame.startNewGame();
        mFirstCardPosition = -1;
        mSecondCardPosition = -1;

        mFlipCounter = 0;


    }

    private List<GameCard> makeGameCards(GameRecord gameRecord) {
        List<GameCard> gameCards = new ArrayList<>(16);
        for (GameCard gameCard : gameRecord.getGameCards()) {
            gameCards.add(new GameCard(gameCard.getImageRef(), this));
            gameCards.add(new GameCard(gameCard.getImageRef(), this));
        }
        return gameCards;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClicked(GameCard clickedImage, View view, int position) {
        if (mFlipCounter == 2) {
            boolean successGuess = mMemoryGame.guess(mFirstCardPosition, mSecondCardPosition);

            if (!successGuess) {
                GameCard firstCard = mAdapter.getmGameCards().get(mFirstCardPosition);
                GameCard secondCard = mAdapter.getmGameCards().get(mSecondCardPosition);
                firstCard.flipCardSide();
                secondCard.flipCardSide();
                mAdapter.notifyItemChanged(mFirstCardPosition);
                mAdapter.notifyItemChanged(mSecondCardPosition);

            } else {
                if (mMemoryGame.isGameOver()) {
                    String messageToDisplay = "total guesses: " + mMemoryGame.getmGuessCounter();
                    //ToDo: display "you win dialog message"

                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("You win!!!");
                    alert.setMessage(messageToDisplay);
                    alert.setPositiveButton("New game", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            initGameCardsToBackAndShuffle();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    alert.setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    });
                    alert.show();


                }
            }
            mFirstCardPosition = -1;
            mSecondCardPosition = -1;
            mFlipCounter = 0;
            return;
        }
        if (clickedImage.isBackSided()) {
            clickedImage.flipCardSide();
            if (mFlipCounter == 0) { //first card flip

                this.mFirstCardPosition = position;
                this.mFlipCounter++;
                mAdapter.notifyItemChanged(mFirstCardPosition);

            } else if (mFlipCounter == 1) { // second card flip

                mFlipCounter++;
                this.mSecondCardPosition = position;

                mAdapter.notifyItemChanged(mSecondCardPosition);
            }

        }
    }
}
