package com.huojieren.healthdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huojieren.healthdiary.MainActivity;
import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.adapter.RecordListAdapter;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.model.Record;

import java.util.List;

public class SummaryFragment extends Fragment {

    private HealthDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        dbHelper = ((MainActivity) getActivity()).getDbHelper();

        // Spinner date_spinner = view.findViewById(R.id.date_spinner);
        RecyclerView rv_record = view.findViewById(R.id.rv_record);
        RecyclerView rv_exercise = view.findViewById(R.id.rv_exercise);
        RecyclerView rv_sleep = view.findViewById(R.id.rv_sleep);

        // TODO 日期选择器
        // date_spinner.setOnItemSelectedListener(new OnItemSelectedListener());

        // 给视图设置数据适配器
        List<Record> dietList = dbHelper.queryRecordByType("diet");
        RecordListAdapter dietAdapter = new RecordListAdapter(dietList);
        rv_record.setAdapter(dietAdapter);

        List<Record> exercisetList = dbHelper.queryRecordByType("exercise");
        RecordListAdapter exerciseAdapter = new RecordListAdapter(exercisetList);
        rv_exercise.setAdapter(exerciseAdapter);

        List<Record> sleepList = dbHelper.queryRecordByType("sleep");
        RecordListAdapter sleepAdapter = new RecordListAdapter(sleepList);
        rv_sleep.setAdapter(sleepAdapter);

        rv_record.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_exercise.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_sleep.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

