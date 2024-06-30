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
import com.huojieren.healthdiary.activity.AddSleepActivity;
import com.huojieren.healthdiary.adapter.SleepAdapter;
import com.huojieren.healthdiary.model.Record;

import java.util.ArrayList;
import java.util.List;

public class SleepFragment extends Fragment {

    private HealthDatabaseHelper dbHelper;
    private RecyclerView RecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep, container, false);

        // 加载sleep记录
        RecyclerView = view.findViewById(R.id.sleep_recycler_view);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbHelper = new HealthDatabaseHelper(getActivity());
        loadData();

        // 加载button
        Button addSleepButton = view.findViewById(R.id.add_sleep_button);
        addSleepButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddSleepActivity.class)));

        return view;
    }

    private void loadData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Record> records = fetchRecordsFromDatabase(db);
        SleepAdapter sleepAdapter = new SleepAdapter(records);
        RecyclerView.setAdapter(sleepAdapter);
    }

    private List<Record> fetchRecordsFromDatabase(SQLiteDatabase db) {
        List<Record> records = new ArrayList<>();
        Cursor cursor = db.query("sleep", new String[]{"date", "description"}, null, null, null, null, "date ASC");

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

}
