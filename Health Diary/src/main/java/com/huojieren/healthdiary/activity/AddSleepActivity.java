package com.huojieren.healthdiary.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.huojieren.healthdiary.HealthDatabaseHelper;
import com.huojieren.healthdiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddSleepActivity extends AppCompatActivity {

    private EditText sleepInput;
    private EditText dateInput;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep);

        sleepInput = findViewById(R.id.sleep_input);
        dateInput = findViewById(R.id.date_input);
        dbHelper = new HealthDatabaseHelper(this);

        // 将当前日期设为默认值
        setCurrentDate();

        // 点击日期输入框时弹出选择框
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        findViewById(R.id.save_sleep_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSleepData();
                finish();
            }
        });
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());
        dateInput.setText(currentDate);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String selectedDateString = sdf.format(selectedDate.getTime());
                        dateInput.setText(selectedDateString);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void saveSleepData() {
        String sleepDetails = sleepInput.getText().toString();
        String sleepDate = dateInput.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", sleepDate);
        values.put("description", sleepDetails);
        db.insert("sleep", null, values);
    }
}