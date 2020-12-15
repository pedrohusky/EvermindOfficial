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

    public void setRecord(String record) {
        this.record = record;
    }

    public EverLinkedMap(String contents, String drawLocation, @Nullable String record) {
        this.content = contents;
        this.drawLocation = drawLocation;
        this.record = record;
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

    public EverLinkedMap get() { return this; }

    public boolean isFull() {
        return !drawLocation.equals("▓") && !record.equals("▓");
    }

}
