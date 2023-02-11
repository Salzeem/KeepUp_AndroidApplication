package com.example.android;

public class Appointment {

    private String time;
    private String course;
    private String date;
    private String Title;
    private String description;


    public Appointment(String time, String course, String date, String title, String description) {
        this.time = time;
        this.course = course;
        this.date = date;
        Title = title;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
