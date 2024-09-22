package com.huojieren.healthdiary.model;

import java.io.Serializable;

public class dietRecord implements Serializable {
    private String date;
    private String icon;
    private String description;

    public dietRecord(String date) {
        this.date = date;
    }

    public dietRecord(String date, String icon, String description) {
        this.date = date;
        this.icon = icon;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "dietRecord{" +
                "date='" + date + '\'' +
                ", icon='" + icon + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}