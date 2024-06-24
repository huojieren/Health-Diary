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

public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.ViewHolder> {

    private final List<Record> records;

    public SummaryListAdapter(List<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView;
        private final TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }

        public void bind(Record record) {
            dateTextView.setText(record.getDate());
            descriptionTextView.setText(record.getDescription());
        }
    }
}

