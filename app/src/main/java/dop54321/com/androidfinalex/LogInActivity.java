package dop54321.com.androidfinalex;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LogInActivity extends ActionBarActivity implements View.OnClickListener {

    Button bNewGame;
    Button bLoadGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        MySqlAdapter.build(this);
        initViews();

        setActivityClickListeners();

    }

    private void setActivityClickListeners() {
        bNewGame.setOnClickListener(this);
        bLoadGame.setOnClickListener(this);
    }

    private void initViews() {
        bNewGame = (Button) findViewById(R.id.button_create_new_game);
        bLoadGame = (Button) findViewById(R.id.button_existing_game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.button_create_new_game:
                moveToChooseImagesScreen();
                break;
            case R.id.button_existing_game:

                //open choose game id dialog
                openChooseNumberDialog();
                break;
        }

    }

    private void openChooseNumberDialog() {

        //Prepare choose game id dialog
        Dialog chooseGameIdDialog = new Dialog(this);
        chooseGameIdDialog.setTitle(getString(R.string.dialog_choose_game_id_title));
        chooseGameIdDialog.setContentView(R.layout.dialog_enter_game_id);




        //show dialog
        chooseGameIdDialog.show();


        //Initialize dialog views
        setDialogViews(chooseGameIdDialog);

    }

    private void setDialogViews(final Dialog dialog) {
        final EditText etGameId = (EditText) dialog.findViewById(R.id.editText_choose_number_game_id);
        Button bSubmit = (Button) dialog.findViewById(R.id.button_choose_number_submit);
        Button bCancel = (Button) dialog.findViewById(R.id.button_choose_number_cancel);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameIdString = etGameId.getText().toString();
                int gameId = -1;
                if (gameIdString != null) {
                    gameId = Integer.parseInt(gameIdString);
                    if (isGameExist(gameId)) {
                        //start GameActivity with the number id of the game.
                        moveToGameActivity(gameId);
                    } else {
                        //There is not saved game that match the gameId,
                        //so show Toast message that there is no saved game that match this game id
                        String noMatchedGameMessage = "There is no match for game id: " + gameIdString;
                        Toast.makeText(getApplicationContext(), noMatchedGameMessage, Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), getString(R.string.game_id_must_not_ne_null_error), Toast.LENGTH_LONG).show();
                }

            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private boolean isGameExist(int gameId) {
        return MySqlAdapter.getInstanse().gameAlreadyExist(gameId);
    }

    private void moveToGameActivity(int gameId) {

        //TODO implement moveToGameActivity

        Intent gameActivityIntent=new Intent();
        gameActivityIntent.setClass(this,GameActivity.class);
        gameActivityIntent.putExtra(GameActivity.GAME_ID_EXTRA,gameId);
        startActivity(gameActivityIntent);


        //TODO remove toast
        Toast.makeText(getApplicationContext(), "Moving to game activity, id:" + gameId, Toast.LENGTH_LONG).show();
    }


    private void moveToChooseImagesScreen() {

        Intent i=new Intent(this,ChooseImagesActivity.class);
        startActivity(i);

    }
}
