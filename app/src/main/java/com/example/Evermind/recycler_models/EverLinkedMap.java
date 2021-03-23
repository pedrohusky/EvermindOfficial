package com.example.Evermind.recycler_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EverLinkedMap {
    @NonNull
    @Override
    public String toString() {
        return "EverLinkedMap{" +
                "content='" + content + '\'' +
                ", drawLocation='" + drawLocation + '\'' +
                ", record='" + record + '\'' +
                '}';
    }

    private String  content = "<br>";
    private String drawLocation = "▓";
    private String record = "▓";
    private List<Integer> audioData = new ArrayList<>();
    @Nullable
    private Integer color;
    private int type;

    public void setRecord(String record) {
        this.record = record;
    }

    public EverLinkedMap(String contents, String drawLocation, String record, List<Integer> audioData, Integer color) {
        this.content = contents;
        this.drawLocation = drawLocation;
        this.record = record;
        this.color = color;
        this.audioData = audioData;
        setType();
    }

    public List<Integer> getAudioData() {
        return audioData;
    }

    public void setAudioData(List<Integer> audioData) {
        this.audioData = audioData;
    }

    private void setType() {
        if (!content.equals("▓")) {
            type = 1;
        }
        if (!this.drawLocation.equals("▓")) {
            type = 2;
        }
        if (this.record != null && !this.record.equals("▓") && !this.record.equals("null")) {
            type = 3;
        }
    }

    public int getType() {
        return type;
    }

    public EverLinkedMap() {
        setType();
    }


    public EverLinkedMap(boolean empty) {
        content = "▓";
        drawLocation = "▓";
        record = "▓";
        setType();

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

    @NonNull
    public EverLinkedMap get() { return this; }

    public boolean isEmpty() {
        return drawLocation.equals("▓") && record.equals("▓") && content.equals("▓") || content.equals("<br>") || content.equals("");
    }

}
