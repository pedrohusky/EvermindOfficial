package com.example.evermemo.recycler_models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class EverLinkedMap implements Serializable {

    private String content = "<br>";
    private String draw = "▓";
    private String audio = "▓";
    @Nullable
    private Integer color;
    private int type;
    private boolean viewNeedUpdate = false;

    public EverLinkedMap(String textContent, String drawContent, String recordContent, Integer color) {
        this.content = textContent;
        this.draw = drawContent;
        this.audio = recordContent;
        this.color = color;
        setType();
    }

    public EverLinkedMap() {
    }

    public boolean isViewNeedUpdate() {
        return viewNeedUpdate;
    }

    public void setViewNeedUpdate(boolean viewNeedUpdate) {
        this.viewNeedUpdate = viewNeedUpdate;
    }

    @Override
    public String toString() {
        return "EverLinkedMap{" +
                "everContent='" + content + '\'' +
                ", everDraw='" + draw + '\'' +
                ", everAudio='" + audio + '\'' +
                ", color=" + color +
                ", type=" + type +
                '}';
    }

    public String getContentByType(int type) {
        String content = "";
        switch (type) {
            case 1:
                content = getContent();
                break;
            case 2:
                content = getDrawLocation();
                break;
            case 3:
                content = getRecord();
                break;
        }
        return content;
    }

    private void setType() {
        if (!content.equals("▓")) {
            type = 1;
        }
        if (!this.draw.equals("▓")) {
            type = 2;
        }
        if (this.audio != null && !this.audio.equals("▓") && !this.audio.equals("null")) {
            type = 3;
        }
    }

    public int getType() {
        if (type == 0) {
            setType();
        }
        return type;
    }

    public String getRecord() {
        return audio;
    }

    public void setRecord(String record) {
        this.audio = record;
        viewNeedUpdate = true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        viewNeedUpdate = true;
    }

    public String getDrawLocation() {
        return draw;
    }

    public void setDrawLocation(String drawLocation) {
        this.draw = drawLocation;
        viewNeedUpdate = true;
    }

    public int getColor() {
        if (color == null) {
            return -1;
        } else {
            return color;
        }
    }

    public void mergeWith(EverLinkedMap oldEver) {
        String oldData = "";
        String content = oldEver.getContent();
        if (content.equals("▓")) {
            content = "";
        }
        oldData = content + "<br>" + this.content;
        this.setContent(oldData);
    }

    public void setColor(int color) {
        this.color = color;
        viewNeedUpdate = true;
    }

    public boolean isEmpty() {
        return draw.equals("▓") && audio.equals("▓") && content.equals("▓") || content.equals("<br>") || content.equals("");
    }

}
