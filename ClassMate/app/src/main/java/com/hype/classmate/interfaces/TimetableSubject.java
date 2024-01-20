package com.hype.classmate.interfaces;

public class TimetableSubject {
    String className, teacher, note, color;

    public TimetableSubject() {
    }

    public TimetableSubject(String className, String teacher, String note, String color) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
