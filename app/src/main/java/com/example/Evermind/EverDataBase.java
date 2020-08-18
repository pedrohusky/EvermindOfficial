package com.example.Evermind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EverDataBase extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NOTES = "Notes";
    private static final String DATABASE_NAME = "NotesDatabase";
    private static final String NOTE_TITLE = "Title";
    private static final String NOTE_CONTENT = "Note_content";
    private static final String NOTE_ID = "ID";
    private static final String NOTE_DATE = "Date";
    private static final String NOTE_BACKGROUND = "Background";
    private static final String NOTE_IMAGEURL = "URL";


    EverDataBase mDatabaseEver;


    public EverDataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //  new Thread(() -> {

        // TODO String createTable = "CREATE TABLE " + TABLE_NOTES + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_DATE + " TEXT)";

        String createTable = "CREATE TABLE " + TABLE_NOTES + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_DATE + " TEXT, " + NOTE_IMAGEURL + " TEXT, " + NOTE_BACKGROUND + " TEXT)";

        db.execSQL(createTable);

        // }).start();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        new Thread(() -> {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
            onCreate(db);
        }).start();
    }


    public boolean AddNoteContent(String title, String content) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_CONTENT, content);
        contentValues.put(NOTE_TITLE, title);
        contentValues.put(NOTE_IMAGEURL, "┼");
        contentValues.put(NOTE_BACKGROUND, "┼");
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.insert(TABLE_NOTES, null, contentValues);

        return true;
    }

    public ArrayList<String> getDateFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListTitles = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                String ImageURL = cursor.getString(4);
                Note_Model note_model = new Note_Model(id, title, content, date, ImageURL);
                arrayListTitles.add(note_model.getDate());
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListTitles;
    }

    public ArrayList<Integer> getIDFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Integer> arrayListTitles = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                String ImageURL = cursor.getString(4);

                Note_Model note_model = new Note_Model(id, title, content, date, ImageURL);
                arrayListTitles.add(note_model.getId());
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListTitles;
    }


    public ArrayList<String> getTitlesFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListTitles = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                String ImageURL = cursor.getString(4);

                Note_Model note_model = new Note_Model(id, title, content, date, ImageURL);
                arrayListTitles.add(note_model.getTitle());
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListTitles;
    }

    public ArrayList<String> getContentsFromDatabase() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListContent = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                String ImageURL = cursor.getString(4);

                Note_Model note_model = new Note_Model(id, title, content, date, ImageURL);
                arrayListContent.add(note_model.getContent());
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();

        return arrayListContent;
    }

    public ArrayList<String> getImageURLFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListURLs = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                String ImageURL = cursor.getString(4);

                Note_Model note_model = new Note_Model(id, title, content, date, ImageURL);
                arrayListURLs.add(note_model.getImageURLS());
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListURLs;
    }

    public String getDateFromDatabaseWithID(Integer ID) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String date = "";
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + ID + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(3);
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return date;
    }

    public Integer getIDFromDatabaseWithID(Integer ID) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Integer noteID = -1;
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + ID + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                noteID = cursor.getInt(0);

            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return noteID;
    }


    public String getTitlesFromDatabaseWithID(Integer ID) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String title = "";
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + ID + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {

                 title = cursor.getString(1);
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return title;
    }

    public String getContentsFromDatabaseWithID(Integer ID) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String content = "";
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + ID + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                 content = cursor.getString(2);
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();

        return content;
    }

    public String getImageURLFromDatabaseWithID(Integer ID) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String ImageURL = "";
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + ID + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                 ImageURL = cursor.getString(4);
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return ImageURL;
    }

    public String getBackgroundFromDatabaseWithID(Integer ID) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String background = "";
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + ID + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
               background = cursor.getString(5);
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return background;
    }

    public void deleteNote(Integer id) {

        new Thread(() -> {

            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + id + "'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

            } else {
            }
        }).start();
    }

    public void editTitle(String id, String title) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE, title);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }

    public void editContentWithID(String id, String content, String oldContent, int editorID) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_CONTENT, content + "┼"+editorID+"┼" + oldContent);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }

    public void editContent(String id, String content) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_CONTENT, content);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }

    public void insertImageToDatabase(String id, String newURL, String oldURL) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_IMAGEURL, newURL + "┼" + oldURL);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }

    public void insertNoteBackgroundToDatabase(String id, String newURL, String oldURL) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_BACKGROUND, newURL + "┼" + oldURL);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }
}
