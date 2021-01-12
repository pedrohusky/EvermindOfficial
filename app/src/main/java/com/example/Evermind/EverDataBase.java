package com.example.Evermind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EverDataBase extends SQLiteOpenHelper {
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
    private final Context context;

    public EverDataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        getAllContentsFromAllNotes();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //  new Thread(() -> {
        String createTable = "CREATE TABLE " + TABLE_NOTES + " (" + NOTE_ID + " TEXT, " + NOTE_TITLE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_DATE + " TEXT, " + NOTE_IMAGEURL + " TEXT, " + NOTE_DRAW + " TEXT, " + NOTE_COLOR + " TEXT, " + NOTE_RECORD + " TEXT)";
        db.execSQL(createTable);
    //    }).start();
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
        new Thread(() -> {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        int p = 0;
        if (cursor.moveToLast()) {
            do {
                ((MainActivity)context).getEverNoteManagement().getNoteModelSection().add(new Note_Model(cursor.getInt(0), p, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
                p++;
            }

            while (cursor.moveToPrevious());
        }
        cursor.close();
        sqLiteDatabase.close();
        }).start();
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

    public void editColor(String id, String color) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLOR, color);
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
}
