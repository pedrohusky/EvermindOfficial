package com.example.Evermind;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.Evermind.recycler_models.EverLinkedMap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mva2.adapter.ListSection;
import mva2.adapter.internal.ItemMetaData;

public class Note_Model extends ItemMetaData implements Parcelable {

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

    public ListSection<EverLinkedMap> everLinkedContents = new ListSection<>();
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
        getEverContents(true);
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

    public void setContents(List<String> contents) {
        new Thread(() -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.content = String.join("", contents);
        } else {
            this.content = contents.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
        this.contents = contents;
        }).start();
    }

    public List<String> getDraws() {
        return draws;
    }

    public void setDraws(List<String> draws) {
        new Thread(() -> {
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
        }).start();
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        new Thread(() -> {
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
        }).start();
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
        for (String path : records) {
            recordsAsString.add(path + "┼");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join("", recordsAsString);
        } else {
            return recordsAsString.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        }
    }

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

    public String getImagesAsString() {
        List<String> imagesAsString = new ArrayList<>();
        for (String path : images) {
            imagesAsString.add(path + "┼");
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
        new Thread(() -> {
        this.content = content;
        contents.clear();
        Collections.addAll(contents, this.content.split("┼"));
        }).start();
    }

    public void setImageURLS(String imageURLS) {
        new Thread(() -> {
        ImageURLS = imageURLS;
        images.clear();
        Collections.addAll(images, ImageURLS.split("┼"));
        }).start();
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

        new Thread(() -> {
        this.drawLocation = drawLocation;
        draws.clear();
        Collections.addAll(draws, this.drawLocation.split("┼"));
        }).start();
    }

    public void addEverLinkedMap(String path, MainActivity mainActivity, boolean isDraw) {
        String s = everLinkedContents.get(everLinkedContents.size() - 1).getContent();
        if (s.equals("") || s.equals("<br>") || s.equals(" ")) {

            new Handler(Looper.getMainLooper()).post(() -> {

                mainActivity.getEverViewManagement().beginDelayedTransition(mainActivity.getCardNoteCreator());

                mainActivity.getNoteCreator().getTextAndDrawRecyclerView().scrollToPosition(everLinkedContents.size());

                everLinkedContents.remove(everLinkedContents.size() - 1);

            });

        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            mainActivity.getEverViewManagement().beginDelayedTransition(mainActivity.getCardNoteCreator());

            if (isDraw) {
                everLinkedContents.add(new EverLinkedMap("▓", path, "▓", null));
            } else {
                everLinkedContents.add(new EverLinkedMap("▓", "▓", path, Integer.parseInt(getNoteColor())));
            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                //   mainActivity.getEverViewManagement().beginDelayedTransition(mainActivity.getCardNoteCreator());
                everLinkedContents.add(new EverLinkedMap());
                everLinkedToString();
                mainActivity.getNoteCreator().getTextAndDrawRecyclerView().scrollToPosition(everLinkedContents.size() - 1);
            }, 350);

        }, 350);
    }

    public void getEverContents(boolean fromNoteScreen) {
        everLinkedContents.clear();
      //  new Thread(() -> {
        int size = contents.size();
        if (fromNoteScreen) {
            if (contents.size() == 3) {
                size = 3;
            }
        }
        for (int i = 0; i < size; i++) {
            everLinkedContents.add(new EverLinkedMap(contents.get(i), draws.get(i), records.get(i), Integer.parseInt(noteColor)));
                if (i + 1 == size) {
                    String contentAtPosition = everLinkedContents.get(i).getContent();
                    if (contentAtPosition.equals("<br>") && i == 1 || contentAtPosition.equals("▓")) {
                        everLinkedContents.add(new EverLinkedMap("", "▓", "▓", Integer.parseInt(noteColor)));
                    }
                }
            }
        }

    public void everLinkedToString() {

        new Thread(() -> {

            List<String> contents = new ArrayList<>();
            List<String> draws = new ArrayList<>();
            List<String> records = new ArrayList<>();

            for (EverLinkedMap e : everLinkedContents.getData()) {
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

        }).start();
    }
}