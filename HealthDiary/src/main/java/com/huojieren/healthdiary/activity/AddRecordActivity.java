package com.huojieren.healthdiary.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
    private static final String ARG_TYPE = "type";
    private String recordType;// 指定记录的类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        et_input_record = findViewById(R.id.et_input_record);
        et_input_date = findViewById(R.id.et_input_date);

        dbHelper = HealthDatabaseHelper.getInstance(this);

        // 从上文中获取记录的类型
        if ((getIntent().getExtras()) != null)
            recordType = (getIntent().getExtras().getString(ARG_TYPE));
        Log.d("AddRecordActivity", "recordType:" + recordType);

        // 将当前日期设为默认值
        setCurrentDate();

        // 点击日期输入框时弹出选择框
        et_input_date.setOnClickListener(v -> showDatePickerDialog());

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            if (saveRecord() == -1) {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            }
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

    private long saveRecord() {
        String recordDate = et_input_date.getText().toString();
        String recordDesc = et_input_record.getText().toString();
        ContentValues record = new ContentValues();
        record.put(recordType + "_date", recordDate);
        record.put(recordType + "_desc", recordDesc);
        Log.d("AddRecordActivity", "record to add:" + record);
        long insertReturnFlag = dbHelper.insertRecord(recordType, record);
        Log.d("AddRecordActivity", "insert return:" + insertReturnFlag);
        return insertReturnFlag;
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
