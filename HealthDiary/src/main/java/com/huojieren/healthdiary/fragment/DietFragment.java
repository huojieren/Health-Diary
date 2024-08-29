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

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.activity.AddDietActivity;
import com.huojieren.healthdiary.adapter.DietAdapter;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.model.Record;

import java.util.ArrayList;
import java.util.List;

public class DietFragment extends Fragment {

    private HealthDatabaseHelper dbHelper;
    private RecyclerView RecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        // 加载diet记录
        RecyclerView = view.findViewById(R.id.diet_recycler_view);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbHelper = new HealthDatabaseHelper(getActivity());
        loadData();

        // 加载button
        Button addDietButton = view.findViewById(R.id.add_diet_button);
        addDietButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddDietActivity.class)));

        return view;
    }

    private void loadData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Record> records = fetchRecordsFromDatabase(db);
        DietAdapter dietAdapter = new DietAdapter(records);
        RecyclerView.setAdapter(dietAdapter);
    }

    private List<Record> fetchRecordsFromDatabase(SQLiteDatabase db) {
        List<Record> records = new ArrayList<>();
        Cursor cursor = db.query("diet", new String[]{"date", "description"}, null, null, null, null, "date ASC");

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
