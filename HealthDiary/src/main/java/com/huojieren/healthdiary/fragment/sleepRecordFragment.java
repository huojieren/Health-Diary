package com.huojieren.healthdiary.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huojieren.healthdiary.MainActivity;
import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.activity.AddSleepRecordActivity;
import com.huojieren.healthdiary.adapter.sleepRecordListAdapter;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.model.sleepRecord;

import java.util.List;

public class sleepRecordFragment extends Fragment {
    private HealthDatabaseHelper dbHelper;
    private ActivityResultLauncher<Intent> addRecordLauncher;
    private sleepRecordListAdapter adapter;
    private List<sleepRecord> mRecordList;

    @NonNull
    public static sleepRecordFragment newInstance() {
        sleepRecordFragment recordFragment = new sleepRecordFragment();
        // 将上文传入的记录类型通过Bundle存储到片段中
        Bundle args = new Bundle();
        args.putString("type", "sleep");
        recordFragment.setArguments(args);
        return recordFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("sleepRecordFragment", "sleepRecordFragment onCreate called");

        // 获取数据库帮助器
        dbHelper = ((MainActivity) getActivity()).getDbHelper();

        // 给 AddRecordActivity 注册回调以便在添加记录后刷新数据
        addRecordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::onActivityResult
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("sleepRecordFragment", "sleepRecordFragment onCreateView called");
        // 获取布局文件的 Java 对象
        View fragment_sleep_record = inflater.inflate(R.layout.fragment_sleep_record, container, false);

        TextView tv_emoji = fragment_sleep_record.findViewById(R.id.tv_emoji);
        TextView tv_title = fragment_sleep_record.findViewById(R.id.tv_title);
        RecyclerView rv_record = fragment_sleep_record.findViewById(R.id.rv_sleep_record);
        Button btn_add_record = fragment_sleep_record.findViewById(R.id.btn_add_record);

        // 设置标题和按钮文字
        tv_emoji.setText("💤");
        tv_title.setText("睡眠");
        String showText = "添加一条睡眠记录";
        btn_add_record.setText(showText);

        Log.d("sleepRecordFragment", dbHelper == null ?
                "after getInstance:dbHelper is null" :
                "after getInstance:dbHelper is not null");

        mRecordList = dbHelper.querySleepRecord();
        Log.d("RecordFragment", "recordList:" + mRecordList.toString());
        adapter = new sleepRecordListAdapter(mRecordList);
        rv_record.setAdapter(adapter);
        rv_record.setLayoutManager(new LinearLayoutManager(getContext()));

        // 给按钮注册点击事件监听
        btn_add_record.setOnClickListener(this::saveBtnOnClick);

        return fragment_sleep_record;
    }

    private void saveBtnOnClick(View v) {
        Intent intent = new Intent(this.getContext(), AddSleepRecordActivity.class);
        // 通过 ActivityResultLauncher 启动
        addRecordLauncher.launch(intent);
    }

    // 获取返回的数据并更新适配器
    @SuppressLint("NotifyDataSetChanged")
    private void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                sleepRecord newRecord = (sleepRecord) data.getSerializableExtra("newRecord");
                // 更新 RecyclerView 数据源，并通知适配器刷新
                mRecordList.add(newRecord);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("RecordFragment", "Received null newRecord");
            }
        }
    }
}
