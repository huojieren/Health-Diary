package com.huojieren.healthdiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.huojieren.healthdiary.model.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HealthDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "health.db";// 数据库名
    private static final String TABLE_DIET = "diet";// 饮食表名
    private static final String TABLE_EXERCISE = "exercise";// 锻炼表名
    private static final String TABLE_SLEEP = "sleep";// 作息表名
    private static final int DATABASE_VERSION = 1;// 数据库版本号
    private static HealthDatabaseHelper mdbHelper = null;// 单例模式获取唯一数据库帮助器实例
    private SQLiteDatabase mReadDB = null;
    private SQLiteDatabase mWriteDB = null;

    private HealthDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 单例模式获取唯一数据库帮助器实例
    public static HealthDatabaseHelper getInstance(Context context) {
        if (mdbHelper == null) {
            mdbHelper = new HealthDatabaseHelper(context);
        }
        return mdbHelper;
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
                "sleep_desc TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP);
        onCreate(db);
    }

    // 打开数据库的读连接
    public void openReadLink() {
        if (mReadDB == null || !mReadDB.isOpen()) {
            mReadDB = mdbHelper.getReadableDatabase();
        }
    }

    // 打开数据库的写连接
    public void openWriteLink() {
        if (mWriteDB == null || !mWriteDB.isOpen()) {
            mWriteDB = mdbHelper.getWritableDatabase();
        }
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mReadDB != null && mReadDB.isOpen()) {
            mReadDB.close();
            mReadDB = null;
        }
        if (mWriteDB != null && mWriteDB.isOpen()) {
            mWriteDB.close();
            mWriteDB = null;
        }
    }

    public long insertRecord(String type, ContentValues record) {
        return mWriteDB.insert(type, null, record);
    }

    public List<Record> queryRecordByType(String type) {
        List<Record> recordList = new ArrayList<>();
        // 从数据库中查询记录
        Log.d("dbHelper", type);
        Cursor cursor = mReadDB.query(type, null, null,
                null, null, null, type + "_date ASC");
        // 将读取到的记录转为列表返回
        while (cursor.moveToNext()) {
            Record record = new Record();
            record.setDate(cursor.getString(1));
            record.setType(type);
            record.setDescription(cursor.getString(2));
            recordList.add(record);
        }
        cursor.close();
        return recordList;
    }

    public List<Record> queryRecordByDateAndType(String type, String[] selectionArgs) {
        List<Record> recordList = new ArrayList<>();
        // 从数据库中查询记录
        Log.d("SummaryFragment", "selections:" + type + "_date = ?");
        Log.d("SummaryFragment", "selectionArgs:" + Arrays.toString(selectionArgs));
        Cursor cursor = mReadDB.query(type, null, type + "_date = ?",
                selectionArgs, null, null, type + "_date ASC");
        // 将读取到的记录转为列表返回
        while (cursor.moveToNext()) {
            Record record = new Record();
            record.setDate(cursor.getString(1));
            record.setType(type);
            record.setDescription(cursor.getString(2));
            recordList.add(record);
        }
        cursor.close();
        return recordList;
    }
}