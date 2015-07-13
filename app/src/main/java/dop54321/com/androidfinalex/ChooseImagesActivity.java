package dop54321.com.androidfinalex;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ChooseImagesActivity extends AppCompatActivity implements GridRecyclerViewAdapter.OnItemClickCallback {

    private static final int RESULT_LOAD_IMG = 1231;


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    GridRecyclerViewAdapter mAdapter;
    List<GameCard> gameCards;

    GameRecord gameRecord = new GameRecord();
    private GameCard clickedCard;
    private int clickedCardPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_images);
        gameCards = new ArrayList<>(16);

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 4);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GridRecyclerViewAdapter(gameRecord.getGameCards(), this);
        mAdapter.setMyCallback(this);

        mRecyclerView.setAdapter(mAdapter);

    }

    private void startPickImageActivity(int position) {
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
                //this.clickedCard.setImageRef(uri);

                //check if image has selected yet
                if (!isUriExist(uri, mAdapter.getmGameCards())) {
                    this.mAdapter.getmGameCards().get(clickedCardPosition).setImageRef(uri);
                    int otherPosition = calculateOtherPosition(clickedCardPosition);
                    this.mAdapter.getmGameCards().get(otherPosition).setImageRef(uri);
                    this.mAdapter.notifyItemChanged(clickedCardPosition);
                    this.mAdapter.notifyItemChanged(otherPosition);
                }else
                    throw new IllegalArgumentException("Image allready in the game.");


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean isUriExist(Uri uri, List<GameCard> gameCards) {
        Boolean exist=false;
        for (GameCard gameCard : gameCards) {
            if (gameCard.getImageRef()!=null) {
                if (gameCard.getImageRef().equals(uri))
                    exist=true;
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
        this.clickedCard = clickedImage;
        this.clickedCardPosition = position;
        startPickImageActivity(position);

    }
}
