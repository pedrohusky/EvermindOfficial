package com.example.evermemo.everUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evermemo.EverRoomNoteDatabase;
import com.example.evermemo.MainActivity;
import com.example.evermemo.Note_Model;
import com.example.evermemo.recycler_models.EverLinkedMap;
import com.firebase.ui.auth.AuthUI;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EverNoteManagement {

    private final List<Note_Model> noteModelSection = new ArrayList<>();
    private final List<Note_Model> allSelected = new ArrayList<>();
    private final List<Note_Model> selectedItems = new ArrayList<>();
    @NonNull
    private final WeakReference<MainActivity> mainActivity;
    private int noteIdIncrement = 0;
    private RecyclerView noteScreenRecycler;
    private int selectedPosition;
    private List<Note_Model> searchListSection = new ArrayList<>();
    private boolean pushed = false;
    private boolean isGrid;
    private boolean searching = false;
    private boolean notesSelected = false;
    private boolean swipeChangeColorRefresh = false;
    private List<EverRoomNoteDatabase.Note> notesList;

    private EverRoomNoteDatabase.AppDatabase everDataBase;

    public EverNoteManagement(@NonNull Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
        //  everDataBase = new EverDataBase(context);
        isGrid = mainActivity.get().getPrefs().getBoolean("isGrid", true);
        mainActivity.get().asyncTask(() -> {

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.AnonymousBuilder().build());
                    mainActivity.get().startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            23);


                    //  everDataBase = Room.databaseBuilder(context, EverRoomNoteDatabase.AppDatabase.class, "notes_Database").build();
                    //notesList = everDataBase.noteManagement().getAll();
                },
                () -> {
                    //  mainActivity.get().runOnUiThread(this::populateListNotes);
                });

    }

    private void populateListNotes() {
        int p = 0;
        for (int i = notesList.size() - 1; i > -1; i--) {
            //   noteModelSection.add(new Note_Model(mainActivity.get(),notesList.get(i).noteName, p, notesList.get(i).noteTitle, Calendar.getInstance().getTime().toString(), notesList.get(i).noteColor, notesList.get(i).noteContent, notesList.get(i).noteImage, notesList.get(i).noteDraw, notesList.get(i).noteRecord));
            p++;
        }

        if (getNoteModelSection().size() > 0) {
            setNoteIdIncrement(mainActivity.get().getPrefs().getInt("lastID", 0));
        }
        mainActivity.get().getNoteScreen().get().init(this, mainActivity.get().getFirebaseHelper());
    }

    public void updateNote(int p, @NotNull Note_Model note, boolean updateListSection) {
        swipeChangeColorRefresh = false;
        if (updateListSection) {
            if (searchListSection.size() > 0) {
                searchListSection.set(p, note);
                //   mainActivity.get().getNoteScreen().get().getAdapter().updateNotesAdapter(searchListSection);
                mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(p);
            } else {
                if (noteModelSection.size() > p) {
                    noteModelSection.set(p, note);
                    //     mainActivity.get().getNoteScreen().get().getAdapter().updateNotesAdapter(noteModelSection);
                    mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(p);
                }
            }
        }

        mainActivity.get().asyncTask(() -> {
                    mainActivity.get().getFirebaseHelper().setDocumentCollection("notes", note.getNote_name(), note, new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("updated sucessfully at " + note.getNote_name());
                        }
                    }, null, null);
                    // everDataBase.noteManagement().updateNote(transformNoteModelToNoteDatabase(note));
                },
                () -> {
                    new Handler(Looper.myLooper()).postDelayed(() -> swipeChangeColorRefresh = false, 150);
                });
    }

    private void deleteDrawsAndImagesInsideNote(@NonNull Note_Model note) {
        for (String path : note.getImages()) {
            File f = new File(path);
            if (f.exists()) {
                mainActivity.get().getFirebaseHelper().deleteFile(f, 0);
                if (f.delete()) {
                    System.out.println("Image at = " + f.getPath() + " Deleted.");
                }
            }
        }
        for (EverLinkedMap ever : note.getEverLinkedContents(false)) {

            String filePath = ever.getContentByType(ever.getType());
            File fD = new File(filePath);
            if (fD.exists()) {
                mainActivity.get().getFirebaseHelper().deleteFile(fD, 0);
                if (fD.delete()) {
                    System.out.println("File at = " + fD.getPath() + " Deleted.");
                }
            }
        }
    }

    public void removeNote(@NonNull Note_Model note) {
        deleteDrawsAndImagesInsideNote(note);
        new Thread(() -> mainActivity.get().getFirebaseHelper().deleteNote(note)).start();
        new Handler(Looper.getMainLooper()).post(() -> {
            noteModelSection.remove(note.getActualPosition());
            for (int position = 0; position < noteModelSection.size(); position++) {
                noteModelSection.get(position).setActualPosition(position);
            }
            mainActivity.get().getNoteScreen().get().getAdapter().updateNotesAdapter(noteModelSection);
        });
    }

    public void addNote(@NonNull Note_Model newNote) {
        //   everDataBase.AddNoteContent(newNote.getId(), newNote.getTitle(), newNote.getContentsAsString(), newNote.getImagesAsString(), newNote.getDrawsAsString(), newNote.getNoteColor(), newNote.getRecordsAsString() );
        noteModelSection.add(0, newNote);
        if (noteModelSection.size() > 1) {
            noteScreenRecycler.smoothScrollToPosition(0);
        }
        for (int position = 0; position < noteModelSection.size(); position++) {
            noteModelSection.get(position).setActualPosition(position);
        }
        mainActivity.get().getNoteScreen().get().getAdapter().updateNotesAdapter(noteModelSection);
        //  new Thread(() -> everDataBase.noteManagement().insert(transformNoteModelToNoteDatabase(newNote))).start();
    }

    public void ChangeNoteColor(int color, @Nullable Note_Model note) {
        if (searchListSection.size() > 0) {
            if (note != null) {
                searchListSection.get(note.getActualPosition()).setNoteColor(String.valueOf(color));
                updateNote(note.getActualPosition(), note, true);
            } else {
                searchListSection.get(selectedPosition).setNoteColor(String.valueOf(color));
                updateNote(selectedPosition, searchListSection.get(selectedPosition), true);
            }
        } else {
            if (note != null) {
                noteModelSection.get(note.getActualPosition()).setNoteColor(String.valueOf(color));
                updateNote(note.getActualPosition(), note, true);
            } else {
                noteModelSection.get(selectedPosition).setNoteColor(String.valueOf(color));
                updateNote(selectedPosition, noteModelSection.get(selectedPosition), true);
            }
        }


    }

    public void handleDeleteSelection() {
        mainActivity.get().asyncTask(() -> {
                    List<Note_Model> list = selectedItems;
                    if (list.size() > 0) {
                        for (Note_Model note : list) {
                            removeNote(note);
                        }
                        selectedItems.clear();
                    }
//                    notesList = everDataBase.noteManagement().getAll();
                },
                () -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
                    mainActivity.get().getEverViewManagement().switchBottomBars("select");
                });
    }

    public void changeColorSelection(int color) {
        //  new Thread(() -> {
        List<Note_Model> list = selectedItems;
        if (list.size() > 0) {
            for (Note_Model note : list) {
                mainActivity.get().setSmoothColorChange(true);
                //     new Handler(Looper.getMainLooper()).post(() -> {
                ChangeNoteColor(color, note);
                //       mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(note.getActualPosition());
                //     });
                note.setSelected(false);
            }
            selectedItems.clear();
        }

        mainActivity.get().getEverViewManagement().switchBottomBars("select");
        new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
        //    }).start();
    }

    public void selectAllNotes() {

        System.out.println("Selecting notes function started.");
        if (notesSelected) {
            System.out.println("Deselecting notes.");
            deselectItems(selectedItems);
            notesSelected = false;
        } else {
            System.out.println("Sorting lists...");
            if (searchListSection.size() > 0) {
                System.out.println("Search list selected..");
                for (Note_Model note : searchListSection) {
                    if (!selectedItems.contains(note)) {
                        System.out.println("Added selected item to selected array.");
                        selectedItems.add(note);
                    }
                }
                // selectedItems.addAll(searchListSection.getData());
            } else {
                System.out.println("Main List selected.");
                for (Note_Model note : noteModelSection) {
                    if (!selectedItems.contains(note)) {
                        System.out.println("Added selected item to selected array.");
                        selectedItems.add(note);
                    }
                }
                // selectedItems.addAll(noteModelSection.getData());
            }
            System.out.println("Trying to select items.");
            selectItems(selectedItems);
            notesSelected = true;
        }
    }

    private void selectItems(@NonNull List<Note_Model> notes) {
        mainActivity.get().asyncTask(null, () -> {
            for (int i = 0; i < notes.size(); i++) {
                System.out.println("Note at p: " + i + "was selected.");
                notes.get(i).setSelected(true);
                mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(i);
            }
        });
    }

    public int getNoteIdIncrement() {
        return noteIdIncrement;
    }

    public void setNoteIdIncrement(int noteIdIncrement) {
        this.noteIdIncrement = noteIdIncrement;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public boolean isPushed() {
        return pushed;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

    public boolean isGrid() {
        return isGrid;
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public boolean isNotesSelected() {
        return notesSelected;
    }

    public void setNotesSelected(boolean notesSelected) {
        this.notesSelected = notesSelected;
    }

    public boolean isSwipeChangeColorRefresh() {
        return swipeChangeColorRefresh;
    }

    public void setSwipeChangeColorRefresh(boolean swipeChangeColorRefresh) {
        this.swipeChangeColorRefresh = swipeChangeColorRefresh;
    }

    @NonNull
    public List<Note_Model> getNoteModelSection() {
        return noteModelSection;
    }

    @NonNull
    public List<Note_Model> getSearchListSection() {
        return searchListSection;
    }

    public void setSearchListSection(List<Note_Model> searchListSection) {
        this.searchListSection = searchListSection;
    }

    public RecyclerView getNoteScreenRecycler() {
        return noteScreenRecycler;
    }

    public void setNoteScreenRecycler(RecyclerView noteScreenRecycler) {
        this.noteScreenRecycler = noteScreenRecycler;
    }

    public void incrementID() {
        noteIdIncrement++;
    }

    @NonNull
    public List<Note_Model> getAllSelected() {
        return allSelected;
    }

    @NonNull
    public List<Note_Model> getSelectedItems() {
        return selectedItems;
    }

    private void deselectItems(@NonNull List<Note_Model> notes) {
        mainActivity.get().asyncTask(null,
                () -> {
                    for (int i = 0; i < notes.size(); i++) {
                        mainActivity.get().setSmoothColorChange(true);
                        notes.get(i).setSelected(false);
                        mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(i);
                    }

                    //notes.clear();
                    selectedItems.clear();

                });
    }

    @NonNull
    private EverRoomNoteDatabase.Note transformNoteModelToNoteDatabase(@NonNull Note_Model note) {
        EverRoomNoteDatabase.Note noteDB = new EverRoomNoteDatabase.Note();
        //  noteDB.noteID = Integer.parseInt(note.getNote_name());
        noteDB.noteDate = note.getDate();
        noteDB.noteTitle = note.getTitle();
        //   noteDB.noteContent = note.getContentsAsString();
        //    noteDB.noteDraw = note.getDrawsAsString();
        //    noteDB.noteImage = note.getImagesAsString();
        //    noteDB.noteRecord = note.getRecordsAsString();
        noteDB.noteColor = note.getNoteColor();
        return noteDB;
    }
}
