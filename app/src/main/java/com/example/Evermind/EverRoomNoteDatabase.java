package com.example.Evermind;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;



public class EverRoomNoteDatabase {
    private static final String NOTE_TITLE = "Title";
    private static final String NOTE_CONTENT = "Note_content";
   // private static final String NOTE_ID = "ID";
    private static final String NOTE_DATE = "Date";
    private static final String NOTE_DRAW = "Background";
    private static final String NOTE_IMAGEURL = "URL";
    private static final String NOTE_COLOR = "Color";
    private static final String NOTE_RECORD = "Record";


    @Entity
    public static class Note {
        @PrimaryKey
        public int noteID;

        @ColumnInfo(name = NOTE_TITLE)
        public String noteTitle;

        @ColumnInfo(name = NOTE_DATE)
        public String noteDate;

        @ColumnInfo(name = NOTE_CONTENT)
        public String noteContent;

        @ColumnInfo(name = NOTE_DRAW)
        public String noteDraw;

        @ColumnInfo(name = NOTE_IMAGEURL)
        public String noteImage;

        @ColumnInfo(name = NOTE_COLOR)
        public String noteColor;

        @ColumnInfo(name = NOTE_RECORD)
        public String noteRecord;

    }

    @Dao
    public interface NoteManagement {

        @Query("SELECT * FROM Note")
        List<Note> getAll();

        @Query("SELECT * FROM Note WHERE noteID IN (:noteID)")
        List<Note> loadAllByIds(int[] noteID);

        @Query("SELECT * FROM Note WHERE noteID LIKE :noteID LIMIT 1")
        Note findByID(int noteID);

        @Insert
        void insertAll(Note... notes);


        @Insert
        void insert(Note note);

        @Delete
        void delete(Note note);

        @Update
        void updateNote(Note note);

        @Update
        int updateNotes(List<Note> notes);


    }

    @Database(entities = {Note.class}, version = 1)
    public abstract static class AppDatabase extends RoomDatabase {
        public abstract NoteManagement noteManagement();
    }
}
