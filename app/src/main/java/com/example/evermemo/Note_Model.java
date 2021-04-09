package com.example.evermemo;

import androidx.annotation.NonNull;

import com.example.evermemo.recycler_models.EverLinkedMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Note_Model implements Serializable {

    private boolean selected = false;
    private String note_name;
    private List<EverLinkedMap> contents = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private int actualPosition;
    private String title;
    private String date;
    private String noteColor = "-1";

    public Note_Model(String note_name, int actualPosition, String title, String date, String color, List<EverLinkedMap> contentData) {
        this.note_name = note_name;
        this.actualPosition = actualPosition;
        this.title = title;
        this.date = date;
        if (!color.equals("-1")) {
            noteColor = color;
        }

        this.contents = contentData;

        if (!noteColor.equals("-1")) {
            for (EverLinkedMap e : this.contents) {
                e.setColor(Integer.parseInt(getNoteColor()));
            }
        }
    }

    public Note_Model() {
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<EverLinkedMap> getContents() {
        return contents;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note_Model{" +
                "selected=" + selected +
                ", note_name='" + note_name + '\'' +
                ", contents=" + contents +
                ", images=" + images +
                ", actualPosition=" + actualPosition +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", noteColor='" + noteColor + '\'' +
                '}';
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
        for (EverLinkedMap e : this.contents) {
            e.setColor(Integer.parseInt(this.noteColor));
        }
    }

    public String getNote_name() {
        return note_name;
    }

    public void setNote_name(String note_name) {
        this.note_name = note_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getActualPosition() {
        return actualPosition;
    }

    public void setActualPosition(int actualPosition) {
        this.actualPosition = actualPosition;
    }

    public void addEverLinkedMap(String path, @NonNull MainActivity mainActivity, boolean isDraw) {

        EverLinkedMap temp = contents.get(contents.size() - 1);
        //  if (temp.getContent().equals("") || temp.getContent().equals("<br>") || temp.getContent().equals("▓")) {
        if (temp.isEmpty()) {
            contents.remove(contents.size() - 1);
            System.out.println("Removed last position because it was empty.");
        }

        if (isDraw) {
            contents.add(new EverLinkedMap("▓", path, "▓", -1));
        } else {
            contents.add(new EverLinkedMap("▓", "▓", path, Integer.parseInt(getNoteColor())));
        }
        contents.add(new EverLinkedMap());
        mainActivity.getNoteCreator().updateContents(contents);
        mainActivity.getNoteCreator().gettextRecyclerCreator().scrollToPosition(contents.size() - 1);
    }


    public boolean isContentsEmpty() {
        boolean isTrueEmpty = false;
        for (EverLinkedMap ever : contents) {
            isTrueEmpty = ever.getContentByType(ever.getType()).isEmpty();
        }
        return isTrueEmpty;
    }

    public List<EverLinkedMap> getEverLinkedContents(boolean fromNoteScreen) {

        if (fromNoteScreen) {
            List<EverLinkedMap> bkup = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                if (contents.size() > i) {
                 //   contents.get(i).setColor(Integer.parseInt(noteColor));
                    bkup.add(contents.get(i));
                }
            }
            return bkup;
        } else {
            return contents;
        }

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}