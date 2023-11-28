package com.example.classmate.interfaces;

public class TimetableSubject {
    String className, teacher, note;
    int color;

    public TimetableSubject() {
    }

    public TimetableSubject(String className, String teacher, String note, int color) {
        this.className = className;
        this.teacher = teacher;
        this.note = note;
        this.color = color;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
