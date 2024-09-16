package com.huojieren.healthdiary;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.fragment.RecordFragment;
import com.huojieren.healthdiary.fragment.SummaryFragment;
import com.huojieren.healthdiary.fragment.sleepRecordFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 打开数据库连接
        dbHelper = HealthDatabaseHelper.getInstance(this);
        dbHelper.openReadLink();
        dbHelper.openWriteLink();

        // 为底部的导航栏设置点击监听器来切换不同片段
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(this);

        // 设置默认片段
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    RecordFragment.newInstance("diet")).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.closeLink();
    }

    public HealthDatabaseHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        // 为不同点击动作生成对应的片段
        if (item.getItemId() == R.id.navigation_diet)
            selectedFragment = RecordFragment.newInstance("diet");
        else if (item.getItemId() == R.id.navigation_exercise)
            selectedFragment = RecordFragment.newInstance("exercise");
        else if (item.getItemId() == R.id.navigation_sleep)
            selectedFragment = sleepRecordFragment.newInstance();
        else if (item.getItemId() == R.id.navigation_summary)
            selectedFragment = new SummaryFragment();
        // 断言selectedFragment不为空
        assert selectedFragment != null;
        // 将预制的片段替换为选中的片段
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    }
}
