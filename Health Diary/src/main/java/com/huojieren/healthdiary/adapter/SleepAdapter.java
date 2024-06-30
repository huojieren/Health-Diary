package com.huojieren.healthdiary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.model.Record;

import java.util.ArrayList;
import java.util.List;

public class SleepAdapter extends RecyclerView.Adapter<SleepAdapter.SleepViewHolder> {

    private final List<Record> records;

    public SleepAdapter(List<Record> records) {
        this.records = records != null ? records : new ArrayList<>();
    }

    @NonNull
    @Override
    public SleepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new SleepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SleepViewHolder holder, int position) {
        Record record = records.get(position);
        holder.descriptionTextView.setText(record.getDescription());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class SleepViewHolder extends RecyclerView.ViewHolder {
        final TextView descriptionTextView;

        SleepViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
        }
    }
}