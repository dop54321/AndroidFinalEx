package dop54321.com.androidfinalex;

import android.net.Uri;

/**
 * Created by dop54321 on 13/07/2015.
 */
public class GameCard {
    private Uri imageRef = null;


    public GameCard(Uri imageRef) {
        this.imageRef = imageRef;

    }

    public GameCard() {
    }

    public Uri getImageRef() {
        return imageRef;
    }

    public void setImageRef(Uri imageRef) {
        this.imageRef = imageRef;
    }

}
