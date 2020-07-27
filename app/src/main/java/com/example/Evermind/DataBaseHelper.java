package com.example.Evermind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NOTES = "Notes";
    private static final String DATABASE_NAME = "NotesDatabase";
    private static final String NOTE_TITLE = "Title";
    private static final String NOTE_CONTENT = "Note_content";
    private static final String NOTE_ID = "ID";
    private static final String NOTE_DATE = "Date";
    private static final String TEXT_COLOR = "Text_color";
    private static final String TEXT_COLOR_TEXT = "Text_color_text";
    private static final String BACKGROUND_COLOR = "Background_color";
    private static final String BACKGROUND_COLOR_TEXT = "Background_color_text";
    private static final String STRIKETROUGH = "Striketrough";
    private static final String UNDERLINE = "Underline";
    private static final String SET_SUBSCRIPT = "Set_subscript";
    private static final String CLICKABLE_TEXT = "Clickable_text";
    private static final String CLICKABLE_TEXT_COLOR = "Clickable_text_color";


    DataBaseHelper mDatabaseHelper;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TODO String createTable = "CREATE TABLE " + TABLE_NOTES + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_DATE + " TEXT)";

        String createTable = "CREATE TABLE " + TABLE_NOTES + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_DATE + " TEXT, " + TEXT_COLOR + " INTEGER, " + TEXT_COLOR_TEXT + " TEXT, " + BACKGROUND_COLOR + " INTEGER, " + BACKGROUND_COLOR_TEXT + " TEXT, " + STRIKETROUGH + " TEXT, " + UNDERLINE + " TEXT, " + SET_SUBSCRIPT + " TEXT, " + CLICKABLE_TEXT + " TEXT, " + CLICKABLE_TEXT_COLOR + " INTEGER)";

        db.execSQL(createTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }


    public boolean AddNoteContent(String title, String content, int text_color, String text_color_text, int background_color, String background_color_text, String striketrough, String underline, String set_subscript, String clickable_text, int clickable_text_color) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_CONTENT, content);
        contentValues.put(NOTE_TITLE, title);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        contentValues.put(TEXT_COLOR, text_color);
        contentValues.put(TEXT_COLOR_TEXT, text_color_text);
        contentValues.put(BACKGROUND_COLOR, background_color);
        contentValues.put(BACKGROUND_COLOR_TEXT, background_color_text);
        contentValues.put(STRIKETROUGH, striketrough);
        contentValues.put(UNDERLINE, underline);
        contentValues.put(SET_SUBSCRIPT, set_subscript);
        contentValues.put(CLICKABLE_TEXT, clickable_text);
        contentValues.put(CLICKABLE_TEXT_COLOR, clickable_text_color);

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
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);

                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListTitles.add(note_model.getDate()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
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
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);

                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListTitles.add(note_model.getId()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
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
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);

                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListTitles.add(note_model.getTitle()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
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
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);

                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListContent.add(note_model.getContent()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListContent;
    }


    public ArrayList<Integer> getTextColorFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Integer> arrayListTextColors = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);


                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListTextColors.add(note_model.getText_color()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListTextColors;
    }

    public ArrayList<String> getTextColorTextFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListTextColorsText = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);

                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListTextColorsText.add(note_model.getText_color_text()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListTextColorsText;
    }

    public ArrayList<Integer> getBackgroundColorFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Integer> arrayListBackgroundColors = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);


                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListBackgroundColors.add(note_model.getBackground_color()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListBackgroundColors;
    }

    public ArrayList<String> getBackgroundColorTextFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListBackgroundColorsText = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);


                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListBackgroundColorsText.add(note_model.getBackground_color_text()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListBackgroundColorsText;
    }

    public ArrayList<String> getStriketroughFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListStriketrough = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);


                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListStriketrough.add(note_model.getStriketrough()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListStriketrough;
    }

    public ArrayList<String> getUnderlineFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListUnderline = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);


                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListUnderline.add(note_model.getUnderline()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListUnderline;
    }

    public ArrayList<String> getSetSubscriptFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListSetSubscript = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);


                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListSetSubscript.add(note_model.getSet_subscript()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListSetSubscript;
    }

    public ArrayList<String> getClickableTextFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> arrayListClickableText = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);


                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListClickableText.add(note_model.getClickable_text()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListClickableText;
    }

    public ArrayList<Integer> getClickableTextColorFromDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Integer> arrayListClickableTextColor = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                int text_color = cursor.getInt(4);
                String text_color_text = cursor.getString(5);
                int background_color = cursor.getInt(6);
                String background_color_text = cursor.getString(7);
                String striketrough = cursor.getString(8);
                String underline = cursor.getString(9);
                String set_subscript = cursor.getString(10);
                String clickable_text = cursor.getString(11);
                int clickable_text_color = cursor.getInt(12);


                Note_Model note_model = new Note_Model(id, title, content, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color);
                arrayListClickableTextColor.add(note_model.getClickable_text_color()); }

            while (cursor.moveToNext()); }
        else { // DO NOTHING
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayListClickableTextColor;
    }









    public boolean deleteNote(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NOTES + " WHERE " + NOTE_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean editNote(String id, String title, String content) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_CONTENT, content);
        contentValues.put(NOTE_TITLE, title);
        Date currentTime = Calendar.getInstance().getTime();
        contentValues.put(NOTE_DATE, currentTime.toString());

        sqLiteDatabase.update(TABLE_NOTES, contentValues, "ID = ?", new String[] { id });
        sqLiteDatabase.close();
        return true;

    }

    public String MakeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        return message;
    }
}
