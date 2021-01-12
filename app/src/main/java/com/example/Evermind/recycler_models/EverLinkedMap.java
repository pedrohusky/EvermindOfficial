package com.example.Evermind.recycler_models;

import androidx.annotation.Nullable;

public class EverLinkedMap {
    @Override
    public String toString() {
        return "EverLinkedMap{" +
                "content='" + content + '\'' +
                ", drawLocation='" + drawLocation + '\'' +
                ", record='" + record + '\'' +
                '}';
    }

    private String content;
    private String drawLocation;
    private String record;
    private Integer color;

    public void setRecord(String record) {
        this.record = record;
    }

    public EverLinkedMap(String contents, String drawLocation, @Nullable String record, @Nullable Integer color) {
        this.content = contents;
        this.drawLocation = drawLocation;
        this.record = record;
        this.color = color;
    }

    public EverLinkedMap() {
        content = "<br>";
        drawLocation = "▓";
        record = "▓";

    }

    public EverLinkedMap(boolean empty) {
        content = "▓";
        drawLocation = "▓";
        record = "▓";

    }

    public String getRecord() {
        return record;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDrawLocation(String drawLocation) {
        this.drawLocation = drawLocation;
    }

    public String getContent() {
        return content;
    }

    public String getDrawLocation() {
        return drawLocation;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
         this.color = color;
    }

    public EverLinkedMap get() { return this; }

    public boolean isFull() {
        return !drawLocation.equals("▓") || !record.equals("▓") || !content.equals("▓");
    }

}
