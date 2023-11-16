package com.example.classmate.interfaces;

public class TimetableClass {
    String className, teacher, note;


    public TimetableClass(String className, String teacher, String note) {
        this.className = className;
        this.teacher = teacher;
        this.note = note;
    }

    public TimetableClass() {
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
}
