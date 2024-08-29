package com.huojieren.healthdiary.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.model.Record;

import java.util.ArrayList;
import java.util.List;

public class DietAdapter extends BaseAdapter {

    private List<Record> recordList;

    public DietAdapter(List<Record> recordList) {
        this.recordList = recordList != null ? recordList : new ArrayList<>();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private static class DietViewHolder {
        final TextView descriptionTextView;

        DietViewHolder(View itemView) {
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
        }
    }
}
