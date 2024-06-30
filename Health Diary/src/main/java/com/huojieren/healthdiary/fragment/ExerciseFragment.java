package com.huojieren.healthdiary.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huojieren.healthdiary.HealthDatabaseHelper;
import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.activity.AddExerciseActivity;
import com.huojieren.healthdiary.adapter.ExerciseAdapter;
import com.huojieren.healthdiary.model.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExerciseFragment extends Fragment {

    private HealthDatabaseHelper dbHelper;
    private RecyclerView RecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        // 加载exercise记录
        RecyclerView = view.findViewById(R.id.exercise_recycler_view);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbHelper = new HealthDatabaseHelper(getActivity());
        loadData();

        // 加载button
        Button addExerciseButton = view.findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddExerciseActivity.class)));

        return view;
    }

    private void loadData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Record> records = fetchRecordsFromDatabase(db, "exercise");
        Map<String, List<Record>> groupedRecords = groupRecordsByDate(records);
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(records);
        RecyclerView.setAdapter(exerciseAdapter);
    }

    private List<Record> fetchRecordsFromDatabase(SQLiteDatabase db, String tableName) {
        List<Record> records = new ArrayList<>();
        Cursor cursor = db.query(tableName, new String[]{"date", "description"}, null, null, null, null, "date ASC");

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                records.add(new Record(date, description));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    private Map<String, List<Record>> groupRecordsByDate(List<Record> records) {
        Map<String, List<Record>> groupedRecords = new HashMap<>();
        for (Record record : records) {
            String date = record.getDate();
            if (!groupedRecords.containsKey(date)) {
                groupedRecords.put(date, new ArrayList<>());
            }
            Objects.requireNonNull(groupedRecords.get(date)).add(record);
        }
        return groupedRecords;
    }
}
