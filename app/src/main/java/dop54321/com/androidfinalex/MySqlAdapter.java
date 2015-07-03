package dop54321.com.androidfinalex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dop54321 on 03/07/2015.
 */
public class MySqlAdapter {

    public static final int URI_LIST_SIZE = 8;
    MySqlHelper mySqlHelper;

    public MySqlAdapter(Context context) {
        mySqlHelper = new MySqlHelper(context);
    }

    private long insertGameRecord(int gameId, List<Uri> imageUris) {
        ContentValues contentValues = new ContentValues();
        long id = -1;
        if (imageUris.size() == URI_LIST_SIZE && legalGameId(gameId)) {

            SQLiteDatabase db = mySqlHelper.getWritableDatabase();

            contentValues.put(MySqlHelper.GID, gameId);
            contentValues.put(MySqlHelper.URI1, imageUris.get(1).toString());
            contentValues.put(MySqlHelper.URI2, imageUris.get(2).toString());
            contentValues.put(MySqlHelper.URI3, imageUris.get(3).toString());
            contentValues.put(MySqlHelper.URI4, imageUris.get(4).toString());
            contentValues.put(MySqlHelper.URI5, imageUris.get(5).toString());
            contentValues.put(MySqlHelper.URI6, imageUris.get(6).toString());
            contentValues.put(MySqlHelper.URI7, imageUris.get(7).toString());
            contentValues.put(MySqlHelper.URI8, imageUris.get(8).toString());

            try {
                id = db.insertOrThrow(MySqlHelper.GAME_TABLE_NAME, null, contentValues);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return id;
    }

    public long insertGameRecord(GameRecord gameRecord) {
        return insertGameRecord(gameRecord.gameId, gameRecord.getImageUris());
    }

    public GameRecord getGameRecord(int gameId) {
        GameRecord gameRecord = null;

        SQLiteDatabase db = mySqlHelper.getWritableDatabase();
        String[] columns = {MySqlHelper.GID, MySqlHelper.URI1,
                MySqlHelper.URI2,
                MySqlHelper.URI3,
                MySqlHelper.URI4,
                MySqlHelper.URI5,
                MySqlHelper.URI6,
                MySqlHelper.URI7,
                MySqlHelper.URI8};

        String selection = MySqlHelper.GID + "=?";

        String[] selectionsArgs = {String.valueOf(gameId)};
        Cursor cursor = db.query(MySqlHelper.GAME_TABLE_NAME, columns, selection, selectionsArgs, null, null, null, "1");

        while (cursor.moveToNext()) {
            gameRecord = parseGameRecord(cursor);
        }

        return gameRecord;
    }

    private GameRecord parseGameRecord(Cursor cursor) {
        GameRecord gameRecord;
        int indGid = cursor.getColumnIndex(MySqlHelper.GID);
        int indUri1 = cursor.getColumnIndex(MySqlHelper.URI1);
        int indUri2 = cursor.getColumnIndex(MySqlHelper.URI2);
        int indUri3 = cursor.getColumnIndex(MySqlHelper.URI3);
        int indUri4 = cursor.getColumnIndex(MySqlHelper.URI4);
        int indUri5 = cursor.getColumnIndex(MySqlHelper.URI5);
        int indUri6 = cursor.getColumnIndex(MySqlHelper.URI6);
        int indUri7 = cursor.getColumnIndex(MySqlHelper.URI7);
        int indUri8 = cursor.getColumnIndex(MySqlHelper.URI8);

        List<Uri> imageUris = new ArrayList<>(8);

        int retGameId = cursor.getInt(indGid);
        String uri1 = cursor.getString(indUri1);
        String uri2 = cursor.getString(indUri2);
        String uri3 = cursor.getString(indUri3);
        String uri4 = cursor.getString(indUri4);
        String uri5 = cursor.getString(indUri5);
        String uri6 = cursor.getString(indUri6);
        String uri7 = cursor.getString(indUri7);
        String uri8 = cursor.getString(indUri8);
        imageUris.add(Uri.parse(uri1));
        imageUris.add(Uri.parse(uri2));
        imageUris.add(Uri.parse(uri3));
        imageUris.add(Uri.parse(uri4));
        imageUris.add(Uri.parse(uri5));
        imageUris.add(Uri.parse(uri6));
        imageUris.add(Uri.parse(uri7));
        imageUris.add(Uri.parse(uri8));

        gameRecord = new GameRecord();
        gameRecord.setGameId(retGameId);
        gameRecord.setImageUris(imageUris);
        return gameRecord;
    }

    private boolean legalGameId(int gameId) {

        if (gameId > 0 && !gameAlreadyExist(gameId)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean gameAlreadyExist(int gameId) {
        Boolean exist = false;

        SQLiteDatabase db = mySqlHelper.getWritableDatabase();

        String[] columns = {MySqlHelper.GID};
        String selection = MySqlHelper.GID + "=?";
        String[] selectionsArgs = {String.valueOf(gameId)};
        Cursor cursor = db.query(MySqlHelper.GAME_TABLE_NAME, columns, selection, selectionsArgs, null, null, null, "1");

        while (cursor.moveToNext()) {
            exist = true;
        }

        return false;
    }

    static class MySqlHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "dop54321.db";
        private static final String GAME_TABLE_NAME = "games";
        public static final String DROP_GAME_TABLE = "DROP IF EXISTS " + GAME_TABLE_NAME;
        private static final String GID = "_gId";
        private static final String URI1 = "uri1";
        private static final String URI2 = "uri2";
        private static final String URI3 = "uri3";
        private static final String URI4 = "uri4";
        private static final String URI5 = "uri5";
        private static final String URI6 = "uri6";
        private static final String URI7 = "uri7";
        private static final String URI8 = "uri8";
        public static final String CREATE_GAME_TABLE = "CREATE TABLE " + GAME_TABLE_NAME + " (" +
                GID + " INTEGER PRIMARY KEY, " +
                URI1 + " VARCHAR(255), " +
                URI2 + " VARCHAR(255), " +
                URI3 + " VARCHAR(255), " +
                URI4 + " VARCHAR(255), " +
                URI5 + " VARCHAR(255), " +
                URI6 + " VARCHAR(255), " +
                URI7 + " VARCHAR(255), " +
                URI8 + " VARCHAR(255) "
                + ");";
        private static final int DATABASE_VERSION = 1;

        public MySqlHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_GAME_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_GAME_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            onCreate(db);
        }
    }
}
