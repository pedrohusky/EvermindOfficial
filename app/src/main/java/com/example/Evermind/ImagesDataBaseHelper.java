package com.example.Evermind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ImagesDataBaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_IMAGES = "Notes";
    private static final String IMAGE_ID = "ID";
    private static final String IMAGE_URL = "URL";
    private static final String DATABASE_NAME = "ImagesDataBaseHelper";


    public ImagesDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //  new Thread(() -> {

        // TODO String createTable = "CREATE TABLE " + TABLE_NOTES + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_DATE + " TEXT)";

        String createTable = "CREATE TABLE " + TABLE_IMAGES + " (" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IMAGE_URL + " TEXT)";

        db.execSQL(createTable);

        // }).start();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        new Thread(() -> {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
            onCreate(db);
        }).start();
    }


    public boolean AddImageURLToDatabase(String url) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_URL, url);

        sqLiteDatabase.insert(TABLE_IMAGES, null, contentValues);

        return true;
    }

    public ArrayList<String> getURLFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arraylistURL = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_IMAGES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String URL = cursor.getString(1);
                Image_model image_model = new Image_model(id, URL);
                arraylistURL.add(image_model.getURL());
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arraylistURL;
    }

    public ArrayList<Integer> getIDFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Integer> arraylistID = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_IMAGES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String URL = cursor.getString(1);
                Image_model image_model = new Image_model(id, URL);
                arraylistID.add(image_model.getImageID());
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arraylistID;
    }

    public void deleteNote(Integer id) {

        new Thread(() -> {

            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + TABLE_IMAGES + " WHERE " + IMAGE_ID + " = '" + id + "'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

            } else {
            }
        }).start();
    }

    public void editImageURL(String id, String url) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_URL, url);

        sqLiteDatabase.update(TABLE_IMAGES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }
}
