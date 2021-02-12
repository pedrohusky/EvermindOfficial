package com.example.Evermind;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.Evermind.recycler_models.EverLinkedMap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Note_Model implements Parcelable {

    public static final Creator<Note_Model> CREATOR = new Creator<Note_Model>() {
        @NonNull
        @Override
        public Note_Model createFromParcel(@NonNull Parcel in) {
            return new Note_Model(in);
        }

        @NonNull
        @Override
        public Note_Model[] newArray(int size) {
            return new Note_Model[size];
        }
    };



    private boolean selected = false;
    private final int id;
    private final String content;
    private final String ImageURLS;
    private final String drawLocation;
    private final String record;
    @NonNull
    public List<EverLinkedMap> everLinkedContents = new ArrayList<>();
    private int actualPosition;
    private String title;
    private String date;
    private String noteColor;
    private List<String> contents = new ArrayList<>();
    private List<String> draws = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<String> records = new ArrayList<>();
    protected Note_Model(@NonNull Parcel in) {
        id = in.readInt();
        actualPosition = in.readInt();
        title = in.readString();
        content = in.readString();
        date = in.readString();
        ImageURLS = in.readString();
        drawLocation = in.readString();
        noteColor = in.readString();
        record = in.readString();
        contents = in.createStringArrayList();
        draws = in.createStringArrayList();
        images = in.createStringArrayList();
        records = in.createStringArrayList();
    }
    public Note_Model(int id, int actualPosition, String title, String content, String date, String imageURLS, String drawLocation, String color, String record) {
        this.id = id;
        this.actualPosition = actualPosition;
        this.title = title;
        this.content = content;
        this.date = date;
        ImageURLS = imageURLS;
        this.drawLocation = drawLocation;
        noteColor = color;
        this.record = record;
        Collections.addAll(contents, this.content.split("┼"));
        Collections.addAll(draws, this.drawLocation.split("┼"));
        if (imageURLS.contains("┼")) {
            Collections.addAll(images, ImageURLS.split("┼"));
        }
        Collections.addAll(records, this.record.split("┼"));
        initiateEverLinkedContents();
    }

    @Override
    public int describeContents() {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(actualPosition);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(ImageURLS);
        dest.writeString(drawLocation);
        dest.writeString(noteColor);
        dest.writeString(record);
        dest.writeStringList(contents);
        dest.writeStringList(draws);
        dest.writeStringList(images);
        dest.writeStringList(records);
    }

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
                ", record='" + record + '\'' +
                ", contents=" + contents +
                ", draws=" + draws +
                ", images=" + images +
                ", records=" + records +
                '}';
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(@NonNull List<String> contents) {
        this.contents = contents;
    }

    public List<String> getDraws() {
        return draws;
    }

    public void setDraws(@NonNull List<String> draws) {
        this.draws = draws;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(@NonNull List<String> images) {
        this.images = images;
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

    @NonNull
    public String getRecordsAsString() {
        List<String> recordsAsString = new ArrayList<>();
        for (String path : records) {
            recordsAsString.add(path + "┼");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join("", recordsAsString);
        } else {
            return recordsAsString.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
    }

    @NonNull
    public String getContentsAsString() {
        List<String> contentAsString = new ArrayList<>();
        for (String path : contents) {
            contentAsString.add(path + "┼");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join("", contentAsString);
        } else {
            return contentAsString.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
    }

    @NonNull
    public String getDrawsAsString() {
        List<String> drawsAsString = new ArrayList<>();
        for (String path : draws) {
            if (path.equals("")) {
                path = "▓";
            }
            drawsAsString.add(path + "┼");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join("", drawsAsString);
        } else {
            return drawsAsString.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
    }

    @NonNull
    public String getImagesAsString() {
        List<String> imagesAsString = new ArrayList<>();
        for (String path : images) {
            if (!path.equals("")) {
                imagesAsString.add(path + "┼");
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join("", imagesAsString);
        } else {
            return imagesAsString.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
    }

    public List<String> getRecords() {
        return records;
    }

    public void setRecords(List<String> records) {
        this.records = records;
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
        String s = everLinkedContents.get(everLinkedContents.size() - 1).getContent();
        if (s.equals("") || s.equals("<br>") || s.equals(" ")) {
            mainActivity.getNoteCreator().gettextRecyclerCreator().scrollToPosition(everLinkedContents.size());
            everLinkedContents.remove(everLinkedContents.size() - 1);

        }
        if (isDraw) {
            everLinkedContents.add(new EverLinkedMap("▓", path, "▓", null));
        } else {
            everLinkedContents.add(new EverLinkedMap("▓", "▓", path, Integer.parseInt(getNoteColor())));
        }
        everLinkedContents.add(new EverLinkedMap());
        everLinkedToString();
        mainActivity.getNoteCreator().updateContents(everLinkedContents);
        mainActivity.getNoteCreator().gettextRecyclerCreator().scrollToPosition(everLinkedContents.size() - 1);
    }

    public void initiateEverLinkedContents() {
        for (int i = 0; i < contents.size(); i++) {
            EverLinkedMap e = new EverLinkedMap(contents.get(i), draws.get(i), records.get(i), Integer.parseInt(noteColor));

            everLinkedContents.add(e);
        }
    }

    public List<EverLinkedMap> getEverLinkedContents(boolean fromNoteScreen) {

        if (fromNoteScreen) {
            List<EverLinkedMap> bkup = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                if (everLinkedContents.size() > i) {
                    bkup.add(everLinkedContents.get(i));
                }
            }
            return bkup;
        } else {
            return everLinkedContents;
        }

    }

    public void everLinkedToString() {

        List<String> contents = new ArrayList<>();
        List<String> draws = new ArrayList<>();
        List<String> records = new ArrayList<>();

        for (EverLinkedMap e : everLinkedContents) {
            if (e.getContent().endsWith("<br>") && e.getContent().length() > 4) {
                String subcontent = e.getContent().substring(0, e.getContent().length() - 4);
                e.setContent(subcontent);
            }
            contents.add(e.getContent());
            draws.add(e.getDrawLocation());
            records.add(e.getRecord());

        }

        setContents(contents);
        setDraws(draws);
        setRecords(records);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isSelected() {
        return selected;
    }
}