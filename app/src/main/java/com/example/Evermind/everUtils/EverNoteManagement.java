package com.example.Evermind.everUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Evermind.EverDataBase;
import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteModelBinder;
import com.example.Evermind.Note_Model;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import mva2.adapter.ListSection;

public class EverNoteManagement {

    public int noteIdIncrement = 0;
    public WeakReference<RecyclerView> noteScreenRecycler;
    public int ID;
    public int selectedPosition;
    public int selectedID;
    public EverDataBase mDatabaseEver;
    public ListSection<Note_Model> noteModelSection = new ListSection<>();
    public ListSection<Note_Model> searchListSection = new ListSection<>();
    public ListSection<Note_Model> allSelected = new ListSection<>();
    public ListSection<Note_Model> selectedItems = new ListSection<>();
    public List<NoteModelBinder.NoteModelViewHolder> holders = new ArrayList<>();
    public boolean pushed = false;
    public boolean isGrid;
    public boolean searching = false;
    public boolean notesSelected = false;
    public boolean swipeChangeColorRefresh = false;
    private final WeakReference<MainActivity> mainActivity;

    public EverNoteManagement(Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
        mDatabaseEver = new EverDataBase(context);
        isGrid = mainActivity.get().prefs.getBoolean("isGrid", true);
    }

    public void updateNote(int p, @NotNull Note_Model note) {
        swipeChangeColorRefresh = true;
        if (searchListSection.size() > 0) {
            new Handler(Looper.getMainLooper()).post(() -> searchListSection.set(p, note));
        } else {
            if (noteModelSection.size() > p) {
                new Handler(Looper.getMainLooper()).post(() -> noteModelSection.set(p, note));
            }
        }

        System.out.println("Content = " + note.getContentsAsString());

        mDatabaseEver.setNoteModel(String.valueOf(note.getId()), note.getTitle(), note.getContentsAsString(), note.getDrawsAsString(), note.getImagesAsString(), note.getNoteColor(), note.getRecordsAsString());
        new Handler(Looper.getMainLooper()).postDelayed(() -> swipeChangeColorRefresh = false, 150);

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
        mDatabaseEver.deleteNote(note.getId());
        new Handler(Looper.getMainLooper()).post(() -> {
            noteModelSection.remove(note.getActualPosition());
            for (int position = 0; position < noteModelSection.size(); position++) {
                noteModelSection.get(position).setActualPosition(position);
            }
        });
    }

    public void addNote(Note_Model newNote) {
        mDatabaseEver.AddNoteContent(newNote.getId(), newNote.getTitle(), newNote.getContentsAsString(), newNote.getImagesAsString(), newNote.getDrawsAsString(), newNote.getNoteColor(), newNote.getRecordsAsString() );
        noteModelSection.add(0, newNote);
        noteScreenRecycler.get().smoothScrollToPosition(0);
        for (int position = 0; position < noteModelSection.size(); position++) {
            noteModelSection.get(position).setActualPosition(position);
        }
    }

    public void ChangeNoteColor(int color) {
        mDatabaseEver.editColor(String.valueOf(selectedID), String.valueOf(color));
        //  notesModels.get(selectedPosition).setNoteColor(String.valueOf(color));
        if (searchListSection.size() > 0) {
            searchListSection.get(selectedPosition).setNoteColor(String.valueOf(color));
            updateNote(selectedPosition, searchListSection.get(selectedPosition));
        } else {
            noteModelSection.get(selectedPosition).setNoteColor(String.valueOf(color));
            updateNote(selectedPosition, noteModelSection.get(selectedPosition));
        }


    }

    public void handleDeleteSelection() {
        new Thread(() -> {
            if (allSelected.size() > 0) {
                List<Note_Model> list = allSelected.getData();
                for (Note_Model note : list) {
                    removeNote(note);
                }
            } else {
                List<Note_Model> list = selectedItems.getData();
                if (list.size() > 0) {
                    for (Note_Model note : list) {
                        removeNote(note);
                    }
                    selectedItems.clear();
                }
            }

            mainActivity.get().CloseOrOpenSelectionOptions(true);
            new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
        }).start();
    }


    public void changeColorSelection(int color) {
        new Thread(() -> {
            if (allSelected.size() > 0) {
                List<Note_Model> list = allSelected.getData();
                for (Note_Model note : list) {
                    System.out.println(note.getActualPosition());
                    selectedPosition = note.getActualPosition();
                    selectedID = note.getId();
                    ChangeNoteColor(color);
                    new Handler(Looper.getMainLooper()).post(() -> mainActivity.get().noteScreen.adapter.get().notifyItemChanged(note.getActualPosition()));
                }
                new Handler(Looper.getMainLooper()).post(() -> deselectItems(allSelected));

            } else {
                List<Note_Model> list = selectedItems.getData();
                if (list.size() > 0) {
                    for (Note_Model note : list) {
                        System.out.println(note.getActualPosition());
                        selectedPosition = note.getActualPosition();
                        selectedID = note.getId();
                        ChangeNoteColor(color);
                        note.setSelected(false);
                        new Handler(Looper.getMainLooper()).post(() -> mainActivity.get().noteScreen.adapter.get().notifyItemChanged(note.getActualPosition()));
                    }
                    selectedItems.clear();
                }
            }

            new Handler(Looper.getMainLooper()).post(() -> mainActivity.get().CloseOrOpenSelectionOptions(true));
            new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
        }).start();
    }


    public void selectAllNotes() {
        if (notesSelected) {
            deselectItems(allSelected);
            notesSelected = false;
        } else {
            if (searchListSection.size() > 0) {
                allSelected.addAll(searchListSection.getData());
            } else {
                allSelected.addAll(noteModelSection.getData());
            }
            selectItems(allSelected);
            notesSelected = true;
        }
    }

    private void selectItems(ListSection<Note_Model> notes) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (int i = 0; i < notes.size(); i++) {
                notes.get(i).setSelected(true);
                mainActivity.get().noteScreen.adapter.get().notifyItemChanged(i);
            }
        });
    }

    private void deselectItems(ListSection<Note_Model> notes) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (int i = 0; i < notes.size(); i++) {
                notes.get(i).setSelected(false);
                mainActivity.get().noteScreen.adapter.get().notifyItemChanged(i);
            }

            notes.clear();
            selectedItems.clear();

        });
    }


}
