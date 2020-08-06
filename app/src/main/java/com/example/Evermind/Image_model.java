package com.example.Evermind;

public class Image_model {


    private int ImageID;
    private String URL;

    public Image_model(int imageID, String URL) {
        ImageID = imageID;
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "Image_model{" +
                "ImageID=" + ImageID +
                ", URL='" + URL + '\'' +
                '}';
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}