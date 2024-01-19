package com.hype.classmate.interfaces;

public class NoteItem {

    String title, body;

    public NoteItem(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public NoteItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
