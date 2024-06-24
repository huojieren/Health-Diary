package com.huojieren.healthdiary.model;

public class Record {
    private String date;
    private String type;
    private String description;

    public Record() {
    }

    public Record(String date, String type, String description) {
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public Record(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

