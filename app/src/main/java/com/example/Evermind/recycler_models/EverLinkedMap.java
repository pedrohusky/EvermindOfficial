package com.example.Evermind.recycler_models;

public class EverLinkedMap {

    private String content;
    private String drawLocation;

    public EverLinkedMap(String contents, String drawLocation) {
        this.content = contents;
        this.drawLocation = drawLocation;
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

}