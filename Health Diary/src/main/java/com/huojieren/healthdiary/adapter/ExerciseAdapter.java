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

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Record> records;

    public ExerciseAdapter(List<Record> records) {
        this.records = records != null ? records : new ArrayList<>();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Record record = records.get(position);
        holder.descriptionTextView.setText(record.getDescription());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView;

        ExerciseViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
        }
    }
}
