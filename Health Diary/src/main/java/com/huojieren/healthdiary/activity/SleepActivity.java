package com.huojieren.healthdiary.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.huojieren.healthdiary.HealthDatabaseHelper;
import com.huojieren.healthdiary.R;

public class SleepActivity extends AppCompatActivity {

    private EditText sleepInput;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        sleepInput = findViewById(R.id.sleep_input);
        dbHelper = new HealthDatabaseHelper(this);

        findViewById(R.id.save_sleep_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSleepData();
                startActivity(new Intent(SleepActivity.this, SummaryActivity.class));
            }
        });
    }

    private void saveSleepData() {
        String sleepDetails = sleepInput.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", "2024-06-12");
        values.put("description", sleepDetails);
        db.insert("sleep", null, values);
    }
}
