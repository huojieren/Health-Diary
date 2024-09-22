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
import com.huojieren.healthdiary.activity.AddRecordActivity;
import com.huojieren.healthdiary.adapter.RecordListAdapter;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.model.Record;

import java.util.List;

public class RecordFragment extends Fragment {

    private static final String ARG_TYPE = "type";
    private String fragmentType;// 指定当前片段处理的记录类型
    private HealthDatabaseHelper dbHelper;
    private ActivityResultLauncher<Intent> addRecordLauncher;
    private RecordListAdapter recordListAdapter;
    private List<Record> mRecordList;

    @NonNull
    public static RecordFragment newInstance(String type) {
        RecordFragment recordFragment = new RecordFragment();
        // 将上文传入的记录类型通过Bundle存储到片段中
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        recordFragment.setArguments(args);
        return recordFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("RecordFragment flaw", fragmentType + " onCreate called");

        // 固定当前片段处理的记录类型
        if (getArguments() != null) {
            fragmentType = getArguments().getString(ARG_TYPE);
            Log.d("RecordFragment", fragmentType == null ? "fragment is null" : "fragmentType:" + fragmentType);
        }

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
        Log.d("RecordFragment flaw", fragmentType + " onCreateView called");
        // 获取布局文件的 Java 对象
        View fragment_record = inflater.inflate(R.layout.fragment_record, container, false);

        RecyclerView rv_record = fragment_record.findViewById(R.id.rv_record);
        Button btn_add_record = fragment_record.findViewById(R.id.btn_add_record);
        TextView tv_title = fragment_record.findViewById(R.id.tv_title);

        // 根据类型获取对应的资源 ID
        int resId = R.string.diet;// 默认类型为饮食
        switch (fragmentType) {
            case "diet":
                resId = R.string.diet;
                break;
            case "exercise":
                resId = R.string.exercise;
                break;
            case "sleep":
                resId = R.string.sleep;
                break;
        }

        // 设置标题和按钮文字
        tv_title.setText(resId);
        String showText = "添加一条" + getResources().getString(resId) + "记录";
        btn_add_record.setText(showText);

        Log.d("RecordFragment", dbHelper == null ?
                "after getInstance:dbHelper is null" :
                "after getInstance:dbHelper is not null");
        // 根据 fragmentType 来处理不同的逻辑
        mRecordList = dbHelper.queryRecordByType(fragmentType);
        Log.d("RecordFragment", "recordList:" + mRecordList.toString());
        recordListAdapter = new RecordListAdapter(mRecordList);
        rv_record.setAdapter(recordListAdapter);
        rv_record.setLayoutManager(new LinearLayoutManager(getContext()));

        // 给按钮注册点击事件监听
        btn_add_record.setOnClickListener(this::saveBtnOnClick);

        return fragment_record;
    }

    private void saveBtnOnClick(View v) {
        Intent intent = new Intent(this.getContext(), AddRecordActivity.class);
        intent.putExtra(ARG_TYPE, fragmentType);
        // 通过 ActivityResultLauncher 启动
        addRecordLauncher.launch(intent);
    }

    // 获取返回的数据并更新适配器
    @SuppressLint("NotifyDataSetChanged")
    private void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Record newRecord = (Record) data.getSerializableExtra("newRecord");
                // 更新 RecyclerView 数据源，并通知适配器刷新
                mRecordList.add(newRecord);
                recordListAdapter.notifyDataSetChanged();
            } else {
                Log.e("RecordFragment", "Received null newRecord");
            }
        }
    }
}
