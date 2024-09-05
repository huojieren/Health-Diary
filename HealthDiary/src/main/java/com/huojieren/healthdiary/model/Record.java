package com.huojieren.healthdiary.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Record implements Serializable {
    private String date;
    private String type;
    private String description;

    public Record() {
    }

    public Record(String date, String description) {
        this.date = date;
        this.description = description;
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

    @NonNull
    @Override
    public String toString() {
        return "Record{" +
                "date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}