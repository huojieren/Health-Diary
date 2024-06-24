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

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private final List<Record> records;

    public RecordAdapter(List<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = records.get(position);
        holder.dateTextView.setText(record.getDate());
        holder.typeTextView.setText(record.getType());
        holder.descriptionTextView.setText(record.getDescription());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView typeTextView;
        TextView descriptionTextView;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            typeTextView = itemView.findViewById(R.id.type_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
        }
    }
}
