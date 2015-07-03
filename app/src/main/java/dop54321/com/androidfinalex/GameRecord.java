package dop54321.com.androidfinalex;

import android.net.Uri;

import java.util.List;

/**
 * Created by dop54321 on 03/07/2015.
 */
public class GameRecord {
    int gameId;
    List<Uri> imageUris;

    public GameRecord() {
    }

    public GameRecord(int gameId, List<Uri> imageUris) {
        this.gameId = gameId;
        this.imageUris = imageUris;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public List<Uri> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<Uri> imageUris) {
        this.imageUris = imageUris;
    }
}
