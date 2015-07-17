package dop54321.com.androidfinalex;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

/**
 * Created by dop54321 on 13/07/2015.
 */
public class GameCard {
    private Uri mImageRef = null;

    private Uri mFrontSideCard;


    private Uri mBackSideCard;


    public GameCard(Uri imageRef) {
        this.mImageRef = imageRef;

        mFrontSideCard = imageRef;
    }


    public GameCard(Uri imageRef, Context context) {
        this(imageRef);
//        mBackSideCard = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getResources().getResourcePackageName(R.drawable.default_pic) +
//                '/' + context.getResources().getResourceTypeName(R.drawable.default_pic) +
//                '/' + context.getResources().getResourceEntryName(R.drawable.default_pic));


        mBackSideCard=Uri.parse("drawable://"+R.drawable.default_pic);
    }


    public GameCard() {
    }

    public Uri getImageRef() {
        return mImageRef;
    }

    public void setImageRef(Uri imageRef) {
        this.mImageRef = imageRef;
    }

    public Uri getmFrontSideCard() {
        return mFrontSideCard;
    }

    public Uri getmBackSideCard() {
        return mBackSideCard;
    }

    public void setmImageRef(Uri mImageRef) {
        this.mImageRef = mImageRef;
    }

    public Boolean isFrontSided() {
        return mImageRef.equals(mFrontSideCard);
    }

    public Boolean isBackSided() {
        return mImageRef.equals(mBackSideCard);
    }

    public void flipCardSide() {
        if (isBackSided()) {
            mImageRef = mFrontSideCard;
        } else {
            mImageRef = mBackSideCard;
        }
    }

    public void setCardBackSided(){
        mImageRef = mBackSideCard;
    }

}
