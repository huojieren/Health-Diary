package com.huojieren.healthdiary.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.adapter.DietAdapter;
import com.huojieren.healthdiary.adapter.ExerciseAdapter;
import com.huojieren.healthdiary.adapter.SleepAdapter;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.model.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SummaryFragment extends Fragment {

    private HealthDatabaseHelper dbHelper;
    private Spinner dateSpinner;
    private RecyclerView dietRecyclerView;
    private RecyclerView exerciseRecyclerView;
    private RecyclerView sleepRecyclerView;
    private List<String> dates;
    private HashMap<String, List<Record>> dietRecords;
    private HashMap<String, List<Record>> exerciseRecords;
    private HashMap<String, List<Record>> sleepRecords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        dateSpinner = view.findViewById(R.id.date_spinner);
        dietRecyclerView = view.findViewById(R.id.diet_recycler_view);
        exerciseRecyclerView = view.findViewById(R.id.exercise_recycler_view);
        sleepRecyclerView = view.findViewById(R.id.sleep_recycler_view);

        dietRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sleepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHelper = new HealthDatabaseHelper(getActivity());

        loadSummaryData();

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = dates.get(position);
                updateRecyclerViews(selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //todo 总览数据
            }
        });

        return view;
    }

    /**
     * 从数据库中查询记录
     */
    private void loadSummaryData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dates = new ArrayList<>();// 存放日期
        dietRecords = new HashMap<>();// 存放diet记录
        exerciseRecords = new HashMap<>();// 存放exercise记录
        sleepRecords = new HashMap<>();// 存放sleep记录

        // 查询diet表
        Cursor dietCursor = db.query("diet", new String[]{"date", "description"}, null, null, null, null, null);
        while (dietCursor.moveToNext()) {
            String date = dietCursor.getString(dietCursor.getColumnIndexOrThrow("date"));
            String description = dietCursor.getString(dietCursor.getColumnIndexOrThrow("description"));
            if (!dietRecords.containsKey(date)) {
                dietRecords.put(date, new ArrayList<>());
                dates.add(date);
            }
            Objects.requireNonNull(dietRecords.get(date)).add(new Record(date, "Diet", description));
        }
        dietCursor.close();

        // 查询exercise表
        Cursor exerciseCursor = db.query("exercise", new String[]{"date", "description"}, null, null, null, null, null);
        while (exerciseCursor.moveToNext()) {
            String date = exerciseCursor.getString(exerciseCursor.getColumnIndexOrThrow("date"));
            String description = exerciseCursor.getString(exerciseCursor.getColumnIndexOrThrow("description"));
            if (!exerciseRecords.containsKey(date)) {
                exerciseRecords.put(date, new ArrayList<>());
                if (!dates.contains(date)) dates.add(date);
            }
            Objects.requireNonNull(exerciseRecords.get(date)).add(new Record(date, "Exercise", description));
        }
        exerciseCursor.close();

        // 查询sleep表
        Cursor sleepCursor = db.query("sleep", new String[]{"date", "description"}, null, null, null, null, null);
        while (sleepCursor.moveToNext()) {
            String date = sleepCursor.getString(sleepCursor.getColumnIndexOrThrow("date"));
            String description = sleepCursor.getString(sleepCursor.getColumnIndexOrThrow("description"));
            if (!sleepRecords.containsKey(date)) {
                sleepRecords.put(date, new ArrayList<>());
                if (!dates.contains(date)) dates.add(date);
            }
            Objects.requireNonNull(sleepRecords.get(date)).add(new Record(date, "Sleep", description));
        }
        sleepCursor.close();

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, dates);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);
    }

    private void updateRecyclerViews(String date) {

        DietAdapter dietAdapter = new DietAdapter(dietRecords.get(date));
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exerciseRecords.get(date));
        SleepAdapter sleepAdapter = new SleepAdapter(sleepRecords.get(date));

        dietRecyclerView.setAdapter(dietAdapter);
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        sleepRecyclerView.setAdapter(sleepAdapter);
    }
}

