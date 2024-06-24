package com.huojieren.healthdiary.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.huojieren.healthdiary.HealthDatabaseHelper;
import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.model.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private HealthDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);

        // 初始化 HealthDatabaseHelper 对象
        dbHelper = new HealthDatabaseHelper(getActivity().getApplicationContext());

        setupViewPager();

        return view;
    }

    private void setupViewPager() {
        SummaryPagerAdapter adapter = new SummaryPagerAdapter(getChildFragmentManager());

        // 查询数据库获取记录数据，假设 records 是你从数据库中查询到的记录列表
        List<Record> records = fetchRecordsFromDatabase();

        // 按日期和类型分组处理数据
        HashMap<String, List<Record>> groupedRecords = groupRecordsByDateAndType(records);

        // 为每组数据创建对应的 Fragment 并添加到 ViewPager 中
        for (String date : groupedRecords.keySet()) {
            List<Record> groupedRecordsForDate = groupedRecords.get(date);
            SummaryListFragment fragment = SummaryListFragment.newInstance(groupedRecordsForDate);
            adapter.addFragment(fragment, date);
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 从数据库中查询记录
     *
     * @return 记录列表List<Record>
     */
    private List<Record> fetchRecordsFromDatabase() {
        List<Record> recordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询 diet 表
        Cursor dietCursor = db.query("diet", new String[]{"date", "description"}, null, null, null, null, null);
        while (dietCursor.moveToNext()) {
            String date = dietCursor.getString(dietCursor.getColumnIndexOrThrow("date"));
            String description = dietCursor.getString(dietCursor.getColumnIndexOrThrow("description"));
            recordList.add(new Record(date, "Diet", description));
        }
        dietCursor.close();

        // 查询 exercise 表
        Cursor exerciseCursor = db.query("exercise", new String[]{"date", "description"}, null, null, null, null, null);
        while (exerciseCursor.moveToNext()) {
            String date = exerciseCursor.getString(exerciseCursor.getColumnIndexOrThrow("date"));
            String description = exerciseCursor.getString(exerciseCursor.getColumnIndexOrThrow("description"));
            recordList.add(new Record(date, "Exercise", description));
        }
        exerciseCursor.close();

        // 查询 sleep 表
        Cursor sleepCursor = db.query("sleep", new String[]{"date", "description"}, null, null, null, null, null);
        while (sleepCursor.moveToNext()) {
            String date = sleepCursor.getString(sleepCursor.getColumnIndexOrThrow("date"));
            String description = sleepCursor.getString(sleepCursor.getColumnIndexOrThrow("description"));
            recordList.add(new Record(date, "Sleep", description));
        }
        sleepCursor.close();

        return recordList;
    }

    /**
     * 根据日期和类型分组处理记录
     *
     * @return 按日期分组的 HashMap，每个日期对应一个记录列表
     */
    private HashMap<String, List<Record>> groupRecordsByDateAndType(List<Record> records) {
        HashMap<String, HashMap<String, List<Record>>> groupedRecords = new HashMap<>();

        for (Record record : records) {
            String date = record.getDate();
            String type = record.getType();

            if (!groupedRecords.containsKey(date)) {
                groupedRecords.put(date, new HashMap<String, List<Record>>());
            }

            HashMap<String, List<Record>> dateGroup = groupedRecords.get(date);
            if (!dateGroup.containsKey(type)) {
                dateGroup.put(type, new ArrayList<Record>());
            }

            dateGroup.get(type).add(record);
        }

        // 将嵌套的 HashMap 转换为单层 HashMap
        HashMap<String, List<Record>> finalGroupedRecords = new HashMap<>();
        for (String date : groupedRecords.keySet()) {
            List<Record> dateRecords = new ArrayList<>();
            for (String type : groupedRecords.get(date).keySet()) {
                dateRecords.addAll(groupedRecords.get(date).get(type));
            }
            finalGroupedRecords.put(date, dateRecords);
        }

        return finalGroupedRecords;
    }

    // SummaryPagerAdapter 内部类的定义
    private static class SummaryPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public SummaryPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}

