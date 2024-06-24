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

public class DietActivity extends AppCompatActivity {

    private EditText dietInput;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        dietInput = findViewById(R.id.diet_input);
        dbHelper = new HealthDatabaseHelper(this);

        findViewById(R.id.save_diet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDietData();
                startActivity(new Intent(DietActivity.this, ExerciseActivity.class));
            }
        });
    }

    private void saveDietData() {
        String dietDetails = dietInput.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", "2024-06-12");
        values.put("description", dietDetails);
        db.insert("diet", null, values);
    }
}
