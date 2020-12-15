package com.example.Evermind;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.Evermind.recycler_models.EverLinkedMap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mva2.adapter.ListSection;
import mva2.adapter.internal.ItemMetaData;

public class Note_Model extends ItemMetaData implements Parcelable {

    private int id;
    private int actualPosition;
    private String title;
    private String content;
    private String date;
    private String ImageURLS;
    private String drawLocation;
    private String noteColor;
    private String record;
    private List<String> contents = new ArrayList<>();
    private List<String> draws = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<String> records = new ArrayList<>();
    public  ListSection<EverLinkedMap> everLinkedContents = new ListSection<>();

    protected Note_Model(Parcel in) {
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

    public static final Creator<Note_Model> CREATOR = new Creator<Note_Model>() {
        @Override
        public Note_Model createFromParcel(Parcel in) {
            return new Note_Model(in);
        }

        @Override
        public Note_Model[] newArray(int size) {
            return new Note_Model[size];
        }
    };

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
        Collections.addAll(images, ImageURLS.split("┼"));
        Collections.addAll(records, this.record.split("┼"));
        getEverContents();
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

    public String getRecordsAsString() {
        List<String> recordsAsString = new ArrayList<>();
        for (String path: records) {
            recordsAsString.add(path+"┼");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join("", recordsAsString);
        } else {
            return recordsAsString.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
    }

    public String getContentsAsString() {
        List<String> contentAsString = new ArrayList<>();
        for (String path: contents) {
            contentAsString.add(path+"┼");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join("", contentAsString);
        } else {
            return contentAsString.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
    }

    public String getDrawsAsString() {
        List<String> drawsAsString = new ArrayList<>();
        for (String path: draws) {
            drawsAsString.add(path+"┼");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join("", drawsAsString);
        } else {
            return drawsAsString.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
    }

    public String getImagesAsString() {
        List<String> imagesAsString = new ArrayList<>();
        for (String path: images) {
            imagesAsString.add(path+"┼");
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

    public void setContent(String content) {
        this.content = content;
        contents.clear();
        Collections.addAll(contents, this.content.split("┼"));
    }

    public void setImageURLS(String imageURLS) {
        ImageURLS = imageURLS;
        images.clear();
        Collections.addAll(images, ImageURLS.split("┼"));
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

    public void setDrawLocation(String drawLocation) {
        this.drawLocation = drawLocation;
        draws.clear();
        Collections.addAll(draws,  this.drawLocation.split("┼"));
    }

    public void setDraws(List<String> draws) {
      //  List<String> nString = new ArrayList<>();
       // for (String path: this.draws) {
      //      nString.add(path+"┼");
      //  }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.drawLocation = String.join("", draws);
        } else {
            this.drawLocation = draws.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
        this.draws = draws;
    }

    public void setImages(List<String> images) {
      //  List<String> nString = new ArrayList<>();
     //  for (String path: this.images) {
      //      nString.add(path+"┼");
      //  }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.ImageURLS = String.join("", images);
        } else {
            this.ImageURLS = images.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
        this.images = images;
    }

    public void setContents(List<String> contents) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.content = String.join("", contents);
        } else {
            this.content = contents.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
        this.contents = contents;
    }

    @Override
    public int describeContents() {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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

    public void addDraw(String drawLocation, MainActivity mainActivity) {
       String s = everLinkedContents.get(everLinkedContents.size()-1).getContent();
        if (s.equals("") || s.equals("<br>") || s.equals(" ")) {
            mainActivity.beginDelayedTransition(mainActivity.cardNoteCreator.get());
            everLinkedContents.remove(everLinkedContents.size()-1);
        }
        draws.add(drawLocation);
        records.add("▓");
        contents.add("▓");
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mainActivity.beginDelayedTransition(mainActivity.cardNoteCreator.get());
            everLinkedContents.add(new EverLinkedMap(contents.get(contents.size()-1), draws.get(draws.size()-1), records.get(records.size()-1)));
            everLinkedContents.add(new EverLinkedMap());
            }, 350);
    }

    public void addRecord(String audioPath, MainActivity mainActivity) {
        String s = everLinkedContents.get(everLinkedContents.size()-1).getContent();
        if (s.equals("") || s.equals("<br>") || s.equals(" ")) {
            mainActivity.beginDelayedTransition(mainActivity.cardNoteCreator.get());
            everLinkedContents.remove(everLinkedContents.size()-1);
        }

        records.add(audioPath);
        draws.add("▓");
        contents.add("▓");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mainActivity.beginDelayedTransition(mainActivity.cardNoteCreator.get());
            everLinkedContents.add(new EverLinkedMap(contents.get(contents.size()-1), draws.get(draws.size()-1), records.get(records.size()-1)));
            everLinkedContents.add(new EverLinkedMap());
        }, 35);
    }

    public void getEverContents() {
        List<String> bitmaps;
        List<String> records;

        everLinkedContents = new ListSection<>();

        int i;

        bitmaps = getDraws();
        records = getRecords();
        List<String> contentsSplitted = getContents();

        if (contentsSplitted.size() != bitmaps.size()) {
            for (i = contentsSplitted.size(); i < bitmaps.size(); i++) {
                contentsSplitted.add("▓");
            }
        }

        if (bitmaps.size() != contentsSplitted.size()) {
            for (i = bitmaps.size(); i < contentsSplitted.size(); i++) {
                bitmaps.add("▓");
            }
        }
        if (records.size() != contentsSplitted.size()) {
            for (i = records.size(); i < contentsSplitted.size(); i++) {
                records.add("▓");
            }
        }

        for (i = 0; i < contentsSplitted.size(); i++) {
            everLinkedContents.add(new EverLinkedMap(contentsSplitted.get(i), bitmaps.get(i), records.get(i)));
        }

        if (everLinkedContents.size() == 0) {
            everLinkedContents.add(new EverLinkedMap("", "▓", "▓"));
        }
        System.out.println(everLinkedContents.size() + " size " + contents.size() + " contents");

    }
}