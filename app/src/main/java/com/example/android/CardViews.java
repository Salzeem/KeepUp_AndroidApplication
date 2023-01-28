package com.example.android;

public class CardViews {
    private String Description;
    private String name;
    private String course;

    public CardViews(String description, String name, String course) {
        Description = description;
        this.name = name;
        this.course = course;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
