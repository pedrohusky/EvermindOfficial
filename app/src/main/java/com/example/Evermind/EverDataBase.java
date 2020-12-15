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
    private static final String NOTE_DRAW = "Background";
    private static final String NOTE_IMAGEURL = "URL";
    private static final String NOTE_COLOR = "Color";
    private static final String NOTE_RECORD = "Record";
    private final ArrayList<Integer> ids = new ArrayList<>();
    private final ArrayList<String> titles = new ArrayList<>();
    private final ArrayList<String> contents = new ArrayList<>();
    private final ArrayList<String> draws = new ArrayList<>();
    private final ArrayList<String> dates = new ArrayList<>();
    private final ArrayList<String> images = new ArrayList<>();
    private final ArrayList<String> colors = new ArrayList<>();
    private final ArrayList<String> records = new ArrayList<>();

    public EverDataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //  new Thread(() -> {

        // TODO String createTable = "CREATE TABLE " + TABLE_NOTES + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_DATE + " TEXT)";

        String createTable = "CREATE TABLE " + TABLE_NOTES + " (" + NOTE_ID + " TEXT, " + NOTE_TITLE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_DATE + " TEXT, " + NOTE_IMAGEURL + " TEXT, " + NOTE_DRAW + " TEXT, " + NOTE_COLOR + " TEXT, " + NOTE_RECORD + " TEXT)";

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


    public void AddNoteContent(int ID, String title, String content, String images, String draws, String color, String record) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_CONTENT, content);
        contentValues.put(NOTE_ID, ID);
        contentValues.put(NOTE_TITLE, title);
        contentValues.put(NOTE_IMAGEURL, images);
        contentValues.put(NOTE_DRAW, draws);
        contentValues.put(NOTE_COLOR, color);
        contentValues.put(NOTE_RECORD, record);
        contentValues.put(NOTE_DATE, Calendar.getInstance().getTime().toString());

        sqLiteDatabase.insert(TABLE_NOTES, null, contentValues);
    }

    public void getAllContentsFromAllNotes() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListTitles = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        ids.clear();
        titles.clear();
        contents.clear();
        dates.clear();
        images.clear();
        draws.clear();
        colors.clear();
        records.clear();
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0));
                titles.add(cursor.getString(1));
                contents.add(cursor.getString(2));
                dates.add(cursor.getString(3));
                images.add(cursor.getString(4));
                draws.add(cursor.getString(5));
                colors.add(cursor.getString(6));
                records.add(cursor.getString(7));
            }

            while (cursor.moveToNext());
        } else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
    }

    public ArrayList<String> getDateFromDatabase() {
        return dates;
    }

    public ArrayList<Integer> getIDFromDatabase() {
        return ids;
    }


    public ArrayList<String> getTitlesFromDatabase() {
        return titles;
    }

    public ArrayList<String> getContentsFromDatabase() {
        return contents;
    }

    public ArrayList<String> getImageURLFromDatabase() {
        return images;
    }

    public ArrayList<String> getDrawLocationFromDatabase() {
        return draws;
    }

    public ArrayList<String> getNoteColorsFromDatabase() {
        return colors;
    }

    public ArrayList<String> getRecordsFromDatabase() {
        return records;
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

            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + id + "'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {

            } else {
            }
    }

    public void setNoteModel(String id, String title, String content, String draws, String images, String color, String record) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE, title);
        contentValues.put(NOTE_CONTENT, content);
        contentValues.put(NOTE_DRAW, draws);
        contentValues.put(NOTE_IMAGEURL, images);
        contentValues.put(NOTE_COLOR, color);
        contentValues.put(NOTE_RECORD, record);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

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

    public void editColor(String id, String color) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLOR, color);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }

    public void editContentWithID(String id, String content, String oldContent) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_CONTENT, content + "┼" + oldContent);
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

    public void editDraw(String id, String draws) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_DRAW, draws);
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

    public void editImages(String id, String newURL) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_IMAGEURL, newURL);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }

    public void insertNoteBackgroundToDatabase(String id, String newURL, String oldURL) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_DRAW, oldURL + newURL  + "┼");
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());
        System.out.println("Added = " + oldURL + newURL  + "┼");

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[]{id});
        sqLiteDatabase.close();

    }
}
