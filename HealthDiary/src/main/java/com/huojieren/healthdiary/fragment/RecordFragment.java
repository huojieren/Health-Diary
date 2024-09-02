package com.huojieren.healthdiary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        if (getArguments() != null) {
            // 固定当前片段处理的记录类型
            fragmentType = getArguments().getString(ARG_TYPE);
            Log.d("RecordFragment", fragmentType == null ? "fragment is null" : "fragment is not null");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取布局文件的 Java 对象
        View fragment_record = inflater.inflate(R.layout.fragment_record, container, false);

        RecyclerView rv_record = fragment_record.findViewById(R.id.rv_record);
        Button btn_add_record = fragment_record.findViewById(R.id.btn_add_record);
        TextView tv_title = fragment_record.findViewById(R.id.tv_title);

        // 根据类型获取对应的资源 ID
        int resId = R.string.diet;
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

        dbHelper = HealthDatabaseHelper.getInstance(getContext());
        Log.d("RecordFragment", dbHelper == null ? "dbHelper is null" : "dbHelper is not null");
        // 断言dbHelper != null
        assert dbHelper != null;
        dbHelper.openReadLink();
        // 根据 fragmentType 来处理不同的逻辑
        List<Record> recordList = dbHelper.queryRecordByType(fragmentType);
        RecordListAdapter recordListAdapter = new RecordListAdapter(recordList);
        rv_record.setAdapter(recordListAdapter);
        rv_record.setLayoutManager(new LinearLayoutManager(getContext()));

        // 给按钮注册点击事件监听
        btn_add_record.setOnClickListener(this::saveBtnOnClick);

        return fragment_record;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO 数据库close，插入失败
        dbHelper.closeLink();
    }

    private void saveBtnOnClick(View v) {
        Intent intent = new Intent(this.getContext(), AddRecordActivity.class);
        intent.setAction(null);
        intent.putExtra(ARG_TYPE, fragmentType);
        startActivity(intent);
    }

    // TODO 页面刷新
}
