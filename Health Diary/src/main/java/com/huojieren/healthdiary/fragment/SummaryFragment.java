package com.huojieren.healthdiary.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huojieren.healthdiary.HealthDatabaseHelper;
import com.huojieren.healthdiary.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SummaryFragment extends Fragment {

    private HealthDatabaseHelper dbHelper;
    private TextView summaryText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        summaryText = view.findViewById(R.id.summary_text);
        dbHelper = new HealthDatabaseHelper(getActivity());
        loadSummaryData();
        return view;
    }

    private void loadSummaryData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder summary = new StringBuilder();

        // Query for diet
        Cursor dietCursor = db.query("diet", new String[]{"date", "description"}, null, null, null, null, null);
        summary.append("Diet:\n");
        appendRecordsToSummary(dietCursor, summary);

        // Query for exercise
        Cursor exerciseCursor = db.query("exercise", new String[]{"date", "description"}, null, null, null, null, null);
        summary.append("\nExercise:\n");
        appendRecordsToSummary(exerciseCursor, summary);

        // Query for sleep
        Cursor sleepCursor = db.query("sleep", new String[]{"date", "description"}, null, null, null, null, null);
        summary.append("\nSleep:\n");
        appendRecordsToSummary(sleepCursor, summary);

        summaryText.setText(summary.toString());
    }

    private void appendRecordsToSummary(Cursor cursor, StringBuilder summary) {
        if (cursor.moveToFirst()) {
            String currentDate = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                if (!date.equals(currentDate)) {
                    currentDate = date;
                    summary.append("\n").append(date).append(":\n");
                }
                summary.append(description).append("\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
