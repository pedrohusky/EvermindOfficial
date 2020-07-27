package com.example.Evermind;

public class Note_Model {

    private int id;
    private String title;
    private String content;
    private String date;
    private Integer text_color;
    private String text_color_text;
    private Integer background_color;
    private String background_color_text;
    private String striketrough;
    private String underline;
    private String set_subscript;
    private String clickable_text;
    private Integer clickable_text_color;

    // Constructors

    public Note_Model(int id, String title, String content, String date, Integer text_color, String text_color_text, Integer background_color, String background_color_text, String striketrough, String underline, String set_subscript, String clickable_text, Integer clickable_text_color) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.text_color = text_color;
        this.text_color_text = text_color_text;
        this.background_color = background_color;
        this.background_color_text = background_color_text;
        this.striketrough = striketrough;
        this.underline = underline;
        this.set_subscript = set_subscript;
        this.clickable_text = clickable_text;
        this.clickable_text_color = clickable_text_color;
    }

    //ToString

    @Override
    public String toString() {
        return "Note_Model{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", text_color='" + text_color + '\'' +
                ", text_color_text='" + text_color_text + '\'' +
                ", background_color='" + background_color + '\'' +
                ", background_color_text='" + background_color_text + '\'' +
                ", striketrough='" + striketrough + '\'' +
                ", underline='" + underline + '\'' +
                ", set_subscript='" + set_subscript + '\'' +
                ", clickable_text='" + clickable_text + '\'' +
                ", clickable_text_color='" + clickable_text_color + '\'' +
                '}';
    }


//Getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getText_color() {
        return text_color;
    }

    public void setText_color(Integer text_color) {
        this.text_color = text_color;
    }

    public String getText_color_text() {
        return text_color_text;
    }

    public void setText_color_text(String text_color_text) {
        this.text_color_text = text_color_text;
    }

    public Integer getBackground_color() {
        return background_color;
    }

    public void setBackground_color(Integer background_color) {
        this.background_color = background_color;
    }

    public String getBackground_color_text() {
        return background_color_text;
    }

    public void setBackground_color_text(String background_color_text) {
        this.background_color_text = background_color_text;
    }

    public String getStriketrough() {
        return striketrough;
    }

    public void setStriketrough(String striketrough) {
        this.striketrough = striketrough;
    }

    public String getUnderline() {
        return underline;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    public String getSet_subscript() {
        return set_subscript;
    }

    public void setSet_subscript(String set_subscript) {
        this.set_subscript = set_subscript;
    }

    public String getClickable_text() {
        return clickable_text;
    }

    public void setClickable_text(String clickable_text) {
        this.clickable_text = clickable_text;
    }

    public Integer getClickable_text_color() {
        return clickable_text_color;
    }

    public void setClickable_text_color(Integer clickable_text_color) {
        this.clickable_text_color = clickable_text_color;
    }
}
