package com.example.Evermind;

public class Checkboxlist_model {


    private int id;
    private String content;

    public Checkboxlist_model(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Checkboxlist_model{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}