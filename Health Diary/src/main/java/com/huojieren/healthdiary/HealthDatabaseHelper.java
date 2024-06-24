package com.huojieren.healthdiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HealthDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "health.db";
    private static final int DATABASE_VERSION = 1;

    public HealthDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE diet (id INTEGER PRIMARY KEY, date , description TEXT)");
        db.execSQL("CREATE TABLE exercise (id INTEGER PRIMARY KEY, date TEXT, description TEXT)");
        db.execSQL("CREATE TABLE sleep (id INTEGER PRIMARY KEY, date TEXT, description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS diet");
        db.execSQL("DROP TABLE IF EXISTS exercise");
        db.execSQL("DROP TABLE IF EXISTS sleep");
        onCreate(db);
    }
}
