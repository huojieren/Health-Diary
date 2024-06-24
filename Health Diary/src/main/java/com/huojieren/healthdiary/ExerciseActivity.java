package com.huojieren.healthdiary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ExerciseActivity extends AppCompatActivity {

    private EditText exerciseInput;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        exerciseInput = findViewById(R.id.exercise_input);
        dbHelper = new HealthDatabaseHelper(this);

        findViewById(R.id.save_exercise_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExerciseData();
                startActivity(new Intent(ExerciseActivity.this, SleepActivity.class));
            }
        });
    }

    private void saveExerciseData() {
        String exerciseDetails = exerciseInput.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", "2024-06-12");
        values.put("description", exerciseDetails);
        db.insert("exercise", null, values);
    }
}
