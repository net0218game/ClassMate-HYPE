package com.hype.classmate.interfaces;

public class TodoItem {
    String title, subject, category, dueDate, description;
    Boolean isDone;

    public TodoItem() {
    }

    public TodoItem(String title, String subject, String category, String dueDate, String description, Boolean isDone) {
        this.title = title;
        this.subject = subject;
        this.category = category;
        this.dueDate = dueDate;
        this.description = description;
        this.isDone = isDone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }
}
