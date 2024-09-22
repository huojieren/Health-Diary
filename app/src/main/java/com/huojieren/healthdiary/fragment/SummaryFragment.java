package com.huojieren.healthdiary.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huojieren.healthdiary.MainActivity;
import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.adapter.RecordListAdapter;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.model.Record;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SummaryFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private HealthDatabaseHelper dbHelper;
    private TextView tv_date_picker;
    private List<Record> dietList;
    private List<Record> exercisetList;
    private List<Record> sleepList;
    private Calendar selectDate;
    private RecordListAdapter dietAdapter;
    private RecordListAdapter exerciseAdapter;
    private RecordListAdapter sleepAdapter;
    private SimpleDateFormat simpleDateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        dbHelper = ((MainActivity) getActivity()).getDbHelper();

        tv_date_picker = view.findViewById(R.id.tv_date_picker);
        RecyclerView rv_diet = view.findViewById(R.id.rv_record);
        RecyclerView rv_exercise = view.findViewById(R.id.rv_exercise);
        RecyclerView rv_sleep = view.findViewById(R.id.rv_sleep);

        // 获取当前日期对象
        selectDate = Calendar.getInstance();

        // 查询记录时的筛选参数
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String[] selectionArgs = {simpleDateFormat.format(selectDate.getTime())};

        // 将 tv_date_picker 设置为当前日期
        String desc = "📅选择日期：" + simpleDateFormat.format(selectDate.getTime());
        tv_date_picker.setText(desc);

        // 给视图设置数据适配器
        dietList = dbHelper.queryRecordByDateAndType("diet", selectionArgs);
        dietAdapter = new RecordListAdapter(dietList);
        rv_diet.setAdapter(dietAdapter);
        rv_diet.setLayoutManager(new LinearLayoutManager(getContext()));

        exercisetList = dbHelper.queryRecordByDateAndType("exercise", selectionArgs);
        exerciseAdapter = new RecordListAdapter(exercisetList);
        rv_exercise.setAdapter(exerciseAdapter);
        rv_exercise.setLayoutManager(new LinearLayoutManager(getContext()));

        sleepList = dbHelper.queryRecordByDateAndType("sleep", selectionArgs);
        sleepAdapter = new RecordListAdapter(sleepList);
        rv_sleep.setAdapter(sleepAdapter);
        rv_sleep.setLayoutManager(new LinearLayoutManager(getContext()));

        // 设置监听器
        tv_date_picker.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_date_picker) {
            // 将 DatePickerDialog 初始化为当前日期
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    this,
                    selectDate.get(Calendar.YEAR),
                    selectDate.get(Calendar.MONTH),
                    selectDate.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    // 点击日期选择对话框上的确定按钮后出发监听器的 onDateSet 方法
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // 更新 selectedDate 为用户选中的日期
        selectDate.set(year, month, dayOfMonth);

        // 更新日期显示
        String desc = "📅选择日期：" + simpleDateFormat.format(selectDate.getTime());
        tv_date_picker.setText(desc);

        // 设置查询参数
        String[] selectionArgs = {simpleDateFormat.format(selectDate.getTime())};
        Log.d("SummaryFragment", "selectionArgs:" + Arrays.toString(selectionArgs));

        // 更新数据
        dietList.clear();
        dietList.addAll(dbHelper.queryRecordByDateAndType("diet", selectionArgs));
        exercisetList.clear();
        exercisetList.addAll(dbHelper.queryRecordByDateAndType("exercise", selectionArgs));
        sleepList.clear();
        sleepList.addAll(dbHelper.queryRecordByDateAndType("sleep", selectionArgs));

        // 通知适配器数据源更新
        dietAdapter.notifyDataSetChanged();
        exerciseAdapter.notifyDataSetChanged();
        sleepAdapter.notifyDataSetChanged();
    }
}