package com.huojieren.healthdiary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HealthDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "health.db";// 数据库名
    private static final String TABLE_DIET = "diet";// 饮食表名
    private static final String TABLE_EXERCISE = "exercise";// 锻炼表名
    private static final String TABLE_SLEEP = "sleep";// 作息表名
    private static final int DATABASE_VERSION = 1;// 数据库版本号

    public HealthDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立饮食表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DIET +
                " (diet_id INTEGER PRIMARY KEY, " +
                "diet_date TEXT, " +
                "diet_desc TEXT)");
        // 建立锻炼表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXERCISE +
                " (exercise_id INTEGER PRIMARY KEY, " +
                "exercise_date TEXT, " +
                "exercise_desc TEXT)");
        // 建立作息表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SLEEP +
                " (sleep_id INTEGER PRIMARY KEY, " +
                "sleep_date TEXT, " +
                "sleep_description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP);
        onCreate(db);
    }
}
