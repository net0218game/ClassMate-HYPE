package com.hype.classmate.interfaces;

public class TimetableClass {
    String subject, classroom, day, start, end;


    public TimetableClass(String subject, String classroom, String day, String start, String end) {
        this.subject = subject;
        this.classroom = classroom;
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public TimetableClass() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
