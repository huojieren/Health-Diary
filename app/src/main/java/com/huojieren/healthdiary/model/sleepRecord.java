package com.huojieren.healthdiary.model;

import java.io.Serializable;

public class sleepRecord implements Serializable {
    private String date;
    private String description;
    private String sleepTime;
    private String wakeupTime;

    public sleepRecord() {
    }

    public sleepRecord(String date, String description) {
        this.date = date;
        this.description = description;
    }

    public sleepRecord(String date, String description, String sleepTime, String wakeupTime) {
        this.date = date;
        this.description = description;
        this.sleepTime = sleepTime;
        this.wakeupTime = wakeupTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(String wakeupTime) {
        this.wakeupTime = wakeupTime;
    }

    @Override
    public String toString() {
        return "Record{" +
                "date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", sleepTime='" + sleepTime + '\'' +
                ", wakeupTime='" + wakeupTime + '\'' +
                '}';
    }
}