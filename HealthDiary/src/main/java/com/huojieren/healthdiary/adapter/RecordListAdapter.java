package com.huojieren.healthdiary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.model.Record;

import java.util.List;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {

    private final List<Record> mRecordList;// 存储 RecyclerView 中要显示的数据的列表

    // 构造函数，用传入的数据列表初始化适配器
    public RecordListAdapter(List<Record> recordList) {
        this.mRecordList = recordList;
    }

    // 创建新的 RecordViewHolder 对象，代表单个列表项的视图
    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 将 XML 布局文件转化为 View，并创建 RecordViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    // 将数据绑定到 RecordViewHolder 中的视图上
    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        // 根据位置获取数据，并将其绑定到 RecordViewHolder 的视图上
        Record record = mRecordList.get(position);
        holder.dateTextView.setText(record.getDate());
        holder.descriptionTextView.setText(record.getDescription());
    }

    // 返回数据列表的大小
    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    // ViewHolder 类，用于缓存视图引用，减少 findViewById 的调用次数
    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        final TextView dateTextView;
        final TextView descriptionTextView;

        public RecordViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
        }
    }
}
