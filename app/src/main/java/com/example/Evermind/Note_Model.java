package com.example.Evermind;

import android.graphics.Color;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mva2.adapter.internal.ItemMetaData;

public class Note_Model extends ItemMetaData implements Serializable {

    private int id;
    private int actualPosition;
    private String title;
    private String content;
    private String date;
    private String ImageURLS;
    private String drawLocation;
    private String noteColor;
    private List<String> contents = new ArrayList<>();
    private List<String> draws = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    @NotNull
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
                ", contents=" + contents +
                ", draws=" + draws +
                ", images=" + images +
                '}';
    }

    public Note_Model(int id, int actualPosition, String title, String content, String date, String imageURLS, String drawLocation, String color) {
        this.id = id;
        this.actualPosition = actualPosition;
        this.title = title;
        this.content = content;
        this.date = date;
        ImageURLS = imageURLS;
        this.drawLocation = drawLocation;
        noteColor = color;

      Collections.addAll(contents, this.content.split("┼"));
     // Collections.addAll(draws, this.drawLocation.split("┼"));
        for (String s : this.drawLocation.split("┼")) {
            if (!s.equals("")) {
                draws.add(s);
            }
        }
    //  Collections.addAll(images, ImageURLS.split("┼"));
        for (String s : ImageURLS.split("┼")) {
            if (!s.equals("")) {
                images.add(s);
            }
        }
    }

    public List<String> getContents() {
        return contents;
    }

    public List<String> getDraws() {
        return draws;
    }

    public List<String> getImages() {
        return images;
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
        contents.clear();
        Collections.addAll(contents, this.content.split("┼"));
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
        images.clear();
        Collections.addAll(images, ImageURLS.split("┼"));
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
        draws.clear();
        Collections.addAll(draws,  this.drawLocation.split("┼"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDraws(List<String> draws) {
        List<String> nString = new ArrayList<>();
        for (String path: images) {
            nString.add(path+"┼");
        }
        this.drawLocation = String.join("", nString);
        this.draws = draws;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setImages(List<String> images) {
        List<String> nString = new ArrayList<>();
        for (String path: images) {
            nString.add(path+"┼");
        }
        this.ImageURLS = String.join("", nString);
        this.images = images;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setContents(List<String> contents) {
        List<String> nString = new ArrayList<>();
        for (String content: contents) {
            nString.add(content+"┼");
        }
        this.content = String.join("", nString);
        this.contents = contents;
    }
}