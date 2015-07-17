package dop54321.com.androidfinalex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ChooseImagesActivity extends AppCompatActivity implements GridRecyclerViewAdapter.OnItemClickCallback {

    public static final int NUM_OF_CARD_ON_GRID = 16;
    private static final int RESULT_LOAD_IMG = 1231;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    GridRecyclerViewAdapter mAdapter;
    List<GameCard> cardsOnGrid;

    private GameCard clickedCard;
    private int clickedCardPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_images);
        cardsOnGrid = new ArrayList<>(NUM_OF_CARD_ON_GRID);

        initCardsOnGrid();

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 4);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GridRecyclerViewAdapter(this.cardsOnGrid, this);
        mAdapter.setMyCallback(this);

        mRecyclerView.setAdapter(mAdapter);

    }

    private void initCardsOnGrid() {
        for (int i = 0; i < NUM_OF_CARD_ON_GRID; i++) {
            this.cardsOnGrid.add(i, new GameCard());
        }
    }

    private void startPickImageActivity() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");

        //Intent galleryIntent = new Intent(Intent.ACTION_PICK,
        //      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();


                Uri uri = Uri.parse(picturePath);

                //check if image has selected yet (already exist..)
                if (!isUriExist(uri, mAdapter.getmGameCards())) {
                    this.mAdapter.getmGameCards().get(clickedCardPosition).setImageRef(uri);
                    int otherPosition = calculateOtherPosition(clickedCardPosition);
                    this.mAdapter.getmGameCards().get(otherPosition).setImageRef(uri);
                    this.mAdapter.notifyItemChanged(clickedCardPosition);
                    this.mAdapter.notifyItemChanged(otherPosition);
                } else
                    throw new IllegalArgumentException("Image allready in the game.");


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean isUriExist(Uri uri, List<GameCard> gameCards) {
        Boolean exist = false;
        for (GameCard gameCard : gameCards) {
            if (gameCard.getImageRef() != null) {
                if (gameCard.getImageRef().equals(uri))
                    exist = true;
            }
        }
        return exist;
    }

    private int calculateOtherPosition(int clickedCardPosition) {
        int modo = clickedCardPosition % 2;
        if (modo == 0)
            return clickedCardPosition + 1;
        else
            return clickedCardPosition - 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_images, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_complete) {
            if (isAllCardsFilled(this.cardsOnGrid)) {

                //display dialog for enter game id
                //TODO: display dialog for enter game id

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Enter game id");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int gameId = Integer.parseInt(input.getText().toString());
                        MySqlAdapter sqlManager = MySqlAdapter.getInstanse();

                        if (sqlManager.legalGameId(gameId)) {
                            //save game record in local sql

                            GameRecord record = createGameRecord(ChooseImagesActivity.this.cardsOnGrid);
                            record.setGameId(gameId);
                            sqlManager.insertGameRecord(record);
                            Toast.makeText(ChooseImagesActivity.this,"Game: "+gameId+", successfully added",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(ChooseImagesActivity.this,"Game id not legal!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                alert.show();


            } else {
                Toast.makeText(this, getString(R.string.error_toast_not_all_cards_filled), Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }



    private GameRecord createGameRecord(List<GameCard> cardsOnGrid) {
        List<GameCard> recordCards = new ArrayList<>();
        for (int i = 0; i < this.NUM_OF_CARD_ON_GRID; i = i + 2) {
            recordCards.add(cardsOnGrid.get(i));
        }

        GameRecord record = new GameRecord();
        record.setGameCards(recordCards);
        return record;
    }

    private boolean isAllCardsFilled(List<GameCard> cards) {
        Boolean valid = true;
        for (GameCard gameCard : cards) {
            if (gameCard.getImageRef() == null) {
                valid = false;
            }
        }
        return valid;
    }


    @Override
    public void onItemClicked(GameCard clickedImage, View view, int position) {
        this.clickedCard = clickedImage;
        this.clickedCardPosition = position;
        startPickImageActivity();

    }
}
