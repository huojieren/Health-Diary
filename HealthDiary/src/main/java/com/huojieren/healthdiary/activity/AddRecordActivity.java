package com.huojieren.healthdiary.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText et_input_record;
    private EditText et_input_date;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        et_input_record = findViewById(R.id.et_input_record);
        et_input_date = findViewById(R.id.et_input_date);
        dbHelper = new HealthDatabaseHelper(this);

        // 将当前日期设为默认值
        setCurrentDate();

        // 点击日期输入框时弹出选择框
        et_input_date.setOnClickListener(v -> showDatePickerDialog());

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            saveRecord();
            finish();
        });
    }

    // 将当前日期设为默认值
    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());
        et_input_date.setText(currentDate);
    }

    // 点击日期输入框时弹出选择框
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveRecord() {
        // TODO 根据意图传进来的参数判断是哪种record
        // Intent intent;
        String dietDetails = et_input_record.getText().toString();
        String dietDate = et_input_date.getText().toString();  // Get selected date
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", dietDate);
        values.put("description", dietDetails);
        db.insert("diet", null, values);
    }

    // 监听器，回调函数
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDateString = sdf.format(selectedDate.getTime());
        et_input_date.setText(selectedDateString);
    }
}
