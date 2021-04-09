package com.example.evermemo;

import java.io.Serializable;
import java.util.Calendar;

public class userSettings implements Serializable {
    private boolean isGrid = false;
    private boolean darkMode = false;
    private String name;
    private String date = Calendar.getInstance().getTime().toString();
    private int noteCount;

    public userSettings() {
    }

    @Override
    public String toString() {
        return "userSettings{" +
                "isGrid=" + isGrid +
                ", darkMode=" + darkMode +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", noteCount=" + noteCount +
                '}';
    }

    public boolean isGrid() {
        return isGrid;
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(int noteCount) {
        this.noteCount = noteCount;
    }
}
