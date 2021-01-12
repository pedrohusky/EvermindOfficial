package com.example.Evermind.everUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.Evermind.EverDataBase;
import com.example.Evermind.EverRoomNoteDatabase;
import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteModelBinder;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import mva2.adapter.ListSection;

public class EverNoteManagement {

    private int noteIdIncrement = 0;
    private RecyclerView noteScreenRecycler;
    private int ID;
    private int selectedPosition;
    private int selectedID;
    private EverDataBase everDataBases;
    private final ListSection<Note_Model> noteModelSection = new ListSection<>();
    private ListSection<Note_Model> searchListSection = new ListSection<>();
    private final ListSection<Note_Model> allSelected = new ListSection<>();
    private final ListSection<Note_Model> selectedItems = new ListSection<>();
    private boolean pushed = false;
    private boolean isGrid;
    private boolean searching = false;
    private boolean notesSelected = false;
    private boolean swipeChangeColorRefresh = false;
    private final WeakReference<MainActivity> mainActivity;
    private  List<EverRoomNoteDatabase.Note> notesList;

    private EverRoomNoteDatabase.AppDatabase everDataBase;

    public EverNoteManagement(Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
      //  everDataBase = new EverDataBase(context);
        isGrid = mainActivity.get().getPrefs().getBoolean("isGrid", true);
        mainActivity.get().getNoteScreen().get().init(this);
        new Thread(() -> {
            everDataBase = Room.databaseBuilder(context, EverRoomNoteDatabase.AppDatabase.class, "notes_Database").build();
            notesList = everDataBase.noteManagement().getAll();
            mainActivity.get().runOnUiThread(this::populateListNotes);
        }).start();

    }

    private void populateListNotes() {
        int p = 0;
        for (int i = notesList.size()-1 ; i > 0 ; i--) {
            noteModelSection.add(new Note_Model(notesList.get(i).noteID, p, notesList.get(i).noteTitle, notesList.get(i).noteContent, notesList.get(i).noteDate, notesList.get(i).noteImage, notesList.get(i).noteDraw, notesList.get(i).noteColor, notesList.get(i).noteRecord));
            p++;
        }

        if (getNoteModelSection().size() > 0) {
            setNoteIdIncrement(mainActivity.get().getPrefs().getInt("lastID", 0));
        }
    }

    public void updateNote(int p, @NotNull Note_Model note, boolean updateListSection) {
        swipeChangeColorRefresh = true;
        if (updateListSection) {
            if (searchListSection.size() > 0) {
                searchListSection.set(p, note);
            } else {
                if (noteModelSection.size() > p) {
                    noteModelSection.set(p, note);
                }
            }
        }

        new Thread(() -> everDataBase.noteManagement().updateNote(transformNoteModelToNoteDatabase(note))).start();
     // everDataBase.setNoteModel(String.valueOf(note.getId()), note.getTitle(), note.getContentsAsString(), note.getDrawsAsString(), note.getImagesAsString(), note.getNoteColor(), note.getRecordsAsString());
        new Handler(Looper.myLooper()).postDelayed(() -> swipeChangeColorRefresh = false, 150);

        //adapter.notifyItemChanged(p);
    }

    private void deleteDrawsAndImagesInsideNote(Note_Model note) {
        for (String path : note.getImages()) {
            File f = new File(path);
            if (f.exists()) {
                if (f.delete()) {
                    System.out.println("Image at = " + f.getPath() + " Deleted.");
                }
            }
        }
        for (String path : note.getDraws()) {
            File f = new File(path);
            if (f.exists()) {
                if (f.delete()) {
                    System.out.println("Draw at = " + f.getPath() + " Deleted.");
                }
            }
        }
    }

    public void removeNote(Note_Model note) {
        deleteDrawsAndImagesInsideNote(note);
        new Thread(() -> everDataBase.noteManagement().delete(transformNoteModelToNoteDatabase(note))).start();
        new Handler(Looper.getMainLooper()).post(() -> {
            noteModelSection.remove(note.getActualPosition());
            for (int position = 0; position < noteModelSection.size(); position++) {
                noteModelSection.get(position).setActualPosition(position);
            }
        });
    }

    public void addNote(Note_Model newNote) {
     //   everDataBase.AddNoteContent(newNote.getId(), newNote.getTitle(), newNote.getContentsAsString(), newNote.getImagesAsString(), newNote.getDrawsAsString(), newNote.getNoteColor(), newNote.getRecordsAsString() );
        noteModelSection.add(0, newNote);
        noteScreenRecycler.smoothScrollToPosition(0);
        for (int position = 0; position < noteModelSection.size(); position++) {
            noteModelSection.get(position).setActualPosition(position);
        }
        new Thread(() -> everDataBase.noteManagement().insert(transformNoteModelToNoteDatabase(newNote))).start();
    }

    public void ChangeNoteColor(int color, @Nullable Note_Model note) {
        if (searchListSection.size() > 0) {
            if (note != null) {
                searchListSection.get(note.getActualPosition()).setNoteColor(String.valueOf(color));
                updateNote(note.getActualPosition(), note, false);
            } else {
                searchListSection.get(selectedPosition).setNoteColor(String.valueOf(color));
                updateNote(selectedPosition, searchListSection.get(selectedPosition), true);
            }
        } else {
            if (note != null) {
                noteModelSection.get(note.getActualPosition()).setNoteColor(String.valueOf(color));
                updateNote(note.getActualPosition(), note, false);
            } else {
                noteModelSection.get(selectedPosition).setNoteColor(String.valueOf(color));
                updateNote(selectedPosition, noteModelSection.get(selectedPosition), true);
            }
        }


    }

    public void handleDeleteSelection() {
        new Thread(() -> {
                List<Note_Model> list = selectedItems.getData();
                if (list.size() > 0) {
                    for (Note_Model note : list) {
                        removeNote(note);
                    }
                    selectedItems.clear();
                }

            new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
        }).start();
        mainActivity.get().getEverViewManagement().CloseOrOpenSelectionOptions(true);
    }


    public void changeColorSelection(int color) {
        new Thread(() -> {
                List<Note_Model> list = selectedItems.getData();
                if (list.size() > 0) {
                    for (Note_Model note : list) {
                        mainActivity.get().setSmoothColorChange(true);
                        new Handler(Looper.getMainLooper()).post(() -> {
                            ChangeNoteColor(color, note);
                            mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(note.getActualPosition());
                        });
                        note.setSelected(false);
                    }
                    selectedItems.clear();
            }

            new Handler(Looper.getMainLooper()).post(() -> mainActivity.get().getEverViewManagement().CloseOrOpenSelectionOptions(true));
            new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
        }).start();
    }


    public void selectAllNotes() {
        if (notesSelected) {
            deselectItems(selectedItems);
            notesSelected = false;
        } else {
            if (searchListSection.size() > 0) {
                for (Note_Model note : searchListSection.getData()) {
                    if (!selectedItems.getData().contains(note)) {
                        selectedItems.add(note);
                    }
                }
               // selectedItems.addAll(searchListSection.getData());
            } else {
                for (Note_Model note : noteModelSection.getData()) {
                    if (!selectedItems.getData().contains(note)) {
                        selectedItems.add(note);
                    }
                }
               // selectedItems.addAll(noteModelSection.getData());
            }
            selectItems(selectedItems);
            notesSelected = true;
        }
    }

    private void selectItems(ListSection<Note_Model> notes) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (int i = 0; i < notes.size(); i++) {
                notes.get(i).setSelected(true);
                mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(i);
            }
        });
    }

    public int getNoteIdIncrement() {
        return noteIdIncrement;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public int getSelectedID() {
        return selectedID;
    }

    public EverDataBase getEverDataBase() {
        return everDataBases;
    }

    public boolean isPushed() {
        return pushed;
    }

    public boolean isGrid() {
        return isGrid;
    }

    public boolean isSearching() {
        return searching;
    }

    public boolean isNotesSelected() {
        return notesSelected;
    }

    public boolean isSwipeChangeColorRefresh() {
        return swipeChangeColorRefresh;
    }

    public void setNotesSelected(boolean notesSelected) {
        this.notesSelected = notesSelected;
    }

    public ListSection<Note_Model> getNoteModelSection() {
        return noteModelSection;
    }

    public ListSection<Note_Model> getSearchListSection() {
        return searchListSection;
    }

    public void setSearchListSection(ListSection<Note_Model> searchListSection) {
        this.searchListSection = searchListSection;
    }

    public void setNoteScreenRecycler(RecyclerView noteScreenRecycler) {
        this.noteScreenRecycler = noteScreenRecycler;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
    }

    public RecyclerView getNoteScreenRecycler() {
        return noteScreenRecycler;
    }

    public void incrementID() {
        noteIdIncrement++;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setSelectedID(int selectedID) {
        this.selectedID = selectedID;
    }

    public void setSwipeChangeColorRefresh(boolean swipeChangeColorRefresh) {
        this.swipeChangeColorRefresh = swipeChangeColorRefresh;
    }

    public ListSection<Note_Model> getAllSelected() {
        return allSelected;
    }

    public void setNoteIdIncrement(int noteIdIncrement) {
        this.noteIdIncrement = noteIdIncrement;
    }

    public ListSection<Note_Model> getSelectedItems() {
        return selectedItems;
    }

    private void deselectItems(ListSection<Note_Model> notes) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (int i = 0; i < notes.size(); i++) {
                mainActivity.get().setSmoothColorChange(true);
                notes.get(i).setSelected(false);
                mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(i);
            }

            //notes.clear();
            selectedItems.clear();

        });
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

    private EverRoomNoteDatabase.Note transformNoteModelToNoteDatabase(Note_Model note) {
        EverRoomNoteDatabase.Note noteDB = new EverRoomNoteDatabase.Note();
        noteDB.noteID = note.getId();
        noteDB.noteTitle = note.getTitle();
        noteDB.noteContent = note.getContentsAsString();
        noteDB.noteDraw = note.getDrawsAsString();
        noteDB.noteImage = note.getImagesAsString();
        noteDB.noteRecord = note.getRecordsAsString();
        noteDB.noteColor = note.getNoteColor();
        return noteDB;
    }
}
