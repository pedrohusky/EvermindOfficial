package com.example.Evermind.ui.note_screen;

import androidx.lifecycle.ViewModel;

import com.example.Evermind.Note_Model;

public class NotesViewModel extends ViewModel {
    private Note_Model actualNote;

    public Note_Model getActualNote() {
        return actualNote;
    }

    public void setActualNote(Note_Model actualNote) {
        this.actualNote = actualNote;
    }
}