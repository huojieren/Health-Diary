package com.huojieren.healthdiary;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huojieren.healthdiary.fragment.DietFragment;
import com.huojieren.healthdiary.fragment.ExerciseFragment;
import com.huojieren.healthdiary.fragment.SleepFragment;
import com.huojieren.healthdiary.fragment.SummaryFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (R.id.navigation_diet == itemId)
                    selectedFragment = new DietFragment();
                else if (R.id.navigation_exercise == itemId)
                    selectedFragment = new ExerciseFragment();
                else if (R.id.navigation_sleep == itemId)
                    selectedFragment = new SleepFragment();
                else if (R.id.navigation_summary == itemId) {
                    selectedFragment = new SummaryFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DietFragment()).commit();
        }
    }
}
