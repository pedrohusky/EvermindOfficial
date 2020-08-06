package com.example.Evermind;

import android.graphics.Color;

public class Note_Model {

    private int id;
    private String title;
    private String content;
    private String date;
    private String ImageURLS;

    @Override
    public String toString() {
        return "Note_Model{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", ImageURLS='" + ImageURLS + '\'' +
                '}';
    }


    public Note_Model(int id, String title, String content, String date, String imageURLS) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        ImageURLS = imageURLS;
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
}