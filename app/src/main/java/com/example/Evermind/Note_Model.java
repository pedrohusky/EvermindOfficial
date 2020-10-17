package com.example.Evermind;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Note_Model implements Serializable {

    private int id;
    private int actualPosition;
    private String title;
    private String content;
    private String date;
    private String ImageURLS;
    private String drawLocation;

    private String noteColor;

    public Note_Model(int id, int actualPosition, String title, String content, String date, String imageURLS, String drawLocation, String color) {
        this.id = id;
        this.actualPosition = actualPosition;
        this.title = title;
        this.content = content;
        this.date = date;
        ImageURLS = imageURLS;
        this.drawLocation = drawLocation;
        noteColor = color;
    }

    @Override
    public String toString() {
        return "Note_Model{" +
                "id=" + id +
                ", actualPosition=" + actualPosition +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", ImageURLS='" + ImageURLS + '\'' +
                ", drawLocation='" + drawLocation + '\'' +
                ", noteColor='" + noteColor + '\'' +
                '}';
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageURLS() {
        return ImageURLS;
    }

    public void setImageURLS(String imageURLS) {
        ImageURLS = imageURLS;
    }
    public int getActualPosition() {
        return actualPosition;
    }

    public void setActualPosition(int actualPosition) {
        this.actualPosition = actualPosition;
    }

    public String getDrawLocation() {
        return drawLocation;
    }

    public void setDrawLocation(String drawLocation) {
        this.drawLocation = drawLocation;
    }
}