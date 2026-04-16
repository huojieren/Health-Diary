package com.huojieren.healthdiary.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.huojieren.healthdiary.model.Record
import com.huojieren.healthdiary.model.RecordType

class HealthDatabase private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "health.db"
        private const val DATABASE_VERSION = 1

        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getInstance(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HealthDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE diet (id INTEGER PRIMARY KEY, date TEXT, description TEXT)")
        db.execSQL("CREATE TABLE exercise (id INTEGER PRIMARY KEY, date TEXT, description TEXT)")
        db.execSQL("CREATE TABLE sleep (id INTEGER PRIMARY KEY, date TEXT, description TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS diet")
        db.execSQL("DROP TABLE IF EXISTS exercise")
        db.execSQL("DROP TABLE IF EXISTS sleep")
        onCreate(db)
    }

    fun insertRecord(record: Record): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("date", record.date)
            put("description", record.description)
        }
        return db.insert(record.type.tableName, null, values)
    }

    fun getRecords(type: RecordType): List<Record> {
        val db = readableDatabase
        val cursor = db.query(type.tableName, null, null, null, null, null, "id DESC")
        val records = mutableListOf<Record>()
        while (cursor.moveToNext()) {
            records.add(
                Record(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                    date = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    type = type
                )
            )
        }
        cursor.close()
        return records
    }

    fun deleteRecord(id: Long, type: RecordType): Int {
        val db = writableDatabase
        return db.delete(type.tableName, "id = ?", arrayOf(id.toString()))
    }
}