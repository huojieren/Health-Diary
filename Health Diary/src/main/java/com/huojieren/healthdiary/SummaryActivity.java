package com.huojieren.healthdiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    private TextView summaryText;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        summaryText = findViewById(R.id.summary_text);
        dbHelper = new HealthDatabaseHelper(this);

        loadSummaryData();

        findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareSummary();
            }
        });
    }

    private void loadSummaryData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder summary = new StringBuilder();

        Cursor cursor = db.query("diet", new String[]{"date", "description"}, null, null, null, null, null);
        summary.append("Diet:\n");
        while (cursor.moveToNext()) {
            summary.append(cursor.getString(cursor.getColumnIndex("date"))).append(": ")
                    .append(cursor.getString(cursor.getColumnIndex("description"))).append("\n");
        }
        cursor.close();

        cursor = db.query("exercise", new String[]{"date", "description"}, null, null, null, null, null);
        summary.append("\nExercise:\n");
        while (cursor.moveToNext()) {
            summary.append(cursor.getString(cursor.getColumnIndex("date"))).append(": ")
                    .append(cursor.getString(cursor.getColumnIndex("description"))).append("\n");
        }
        cursor.close();

        cursor = db.query("sleep", new String[]{"date", "description"}, null, null, null, null, null);
        summary.append("\nSleep:\n");
        while (cursor.moveToNext()) {
            summary.append(cursor.getString(cursor.getColumnIndex("date"))).append(": ")
                    .append(cursor.getString(cursor.getColumnIndex("description"))).append("\n");
        }
        cursor.close();

        summaryText.setText(summary.toString());
    }

    private void shareSummary() {
        String summary = summaryText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, summary);
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}
