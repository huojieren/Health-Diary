package com.huojieren.healthdiary.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.model.sleepRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddSleepRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private HealthDatabaseHelper dbHelper;
    private sleepRecord newRecord;
    private TextView tv_sleep_start;
    private Button btn_sleep_start;
    private TextView tv_sleep_end;
    private Button btn_sleep_end;
    private Calendar sleepTime;
    private Calendar wakeupTime;
    private boolean isSelectingSleepTime = true;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep_record);

        // 设置 emoji 和标题
        TextView tv_emoji = findViewById(R.id.tv_emoji);
        tv_emoji.setText("💤");
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("记录一次睡眠");

        tv_sleep_start = findViewById(R.id.tv_sleep_start);
        btn_sleep_start = findViewById(R.id.btn_sleep_start);
        tv_sleep_end = findViewById(R.id.tv_sleep_end);
        btn_sleep_end = findViewById(R.id.btn_sleep_end);

        dbHelper = HealthDatabaseHelper.getInstance(this);

        sleepTime = Calendar.getInstance();
        wakeupTime = Calendar.getInstance();
        timeFormat = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // 点击按钮弹出时间选择器
        btn_sleep_start.setOnClickListener(v -> {
            isSelectingSleepTime = true;
            selectTime(sleepTime);
        });
        btn_sleep_end.setOnClickListener(v -> {
            isSelectingSleepTime = false;
            selectTime(wakeupTime);
        });

        // 保存此项记录
        findViewById(R.id.btn_save).setOnClickListener(this::saveOnClick);
    }

    // 弹出日期和时间选择器
    private void selectTime(@NonNull Calendar selectedTime) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                selectedTime.get(Calendar.YEAR),
                selectedTime.get(Calendar.MONTH),
                selectedTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // 日期选择器的监听器
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (isSelectingSleepTime) {
            // 设置 sleepTime 的日期
            sleepTime.set(Calendar.YEAR, year);
            sleepTime.set(Calendar.MONTH, month);
            sleepTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        } else {
            // 设置 wakeupTime 的日期
            wakeupTime.set(Calendar.YEAR, year);
            wakeupTime.set(Calendar.MONTH, month);
            wakeupTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

        // 弹出时间选择框
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                isSelectingSleepTime ?
                        sleepTime.get(Calendar.HOUR_OF_DAY) : wakeupTime.get(Calendar.HOUR_OF_DAY),
                isSelectingSleepTime ?
                        sleepTime.get(Calendar.MINUTE) : wakeupTime.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    // 时间选择器的监听器
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (isSelectingSleepTime) {
            // 设置 sleepTime 的时间
            sleepTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            sleepTime.set(Calendar.MINUTE, minute);
            // 更新显示
            tv_sleep_start.setText(timeFormat.format(sleepTime.getTime()));
        } else {
            // 设置 wakeupTime 的时间
            wakeupTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            wakeupTime.set(Calendar.MINUTE, minute);
            // 更新显示
            tv_sleep_end.setText(timeFormat.format(wakeupTime.getTime()));
        }
    }

    // 保存此项记录
    private long saveRecord() {
        String recordDate = dateFormat.format(sleepTime.getTime());
        String recordSleepTime = timeFormat.format(sleepTime.getTime());
        String recordWakeupTime = timeFormat.format(wakeupTime.getTime());
        Log.d("AddSleepRecordActivity", "recordSleepTime:" + recordSleepTime);
        Log.d("AddSleepRecordActivity", "recordWakeupTime:" + recordWakeupTime);

        // 计算睡眠时间
        long diffInMillis = wakeupTime.getTimeInMillis() - sleepTime.getTimeInMillis();
        String recordDesc = diffInMillis / (1000 * 60 * 60) + "小时" + diffInMillis / (1000 * 60) % 60 + "分";

        ContentValues record = new ContentValues();
        record.put("sleep_date", recordDate);
        record.put("sleep_start", recordSleepTime);
        record.put("sleep_end", recordWakeupTime);
        record.put("sleep_desc", recordDesc);

        newRecord = new sleepRecord(recordDate, recordDesc, recordSleepTime, recordWakeupTime);

        Log.d("AddSleepRecordActivity", "record to add:" + record);
        dbHelper.openWriteLink();
        long insertReturnFlag = dbHelper.insertRecord("sleep", record);
        dbHelper.closeLink();
        Log.d("AddSleepRecordActivity", "insert return:" + insertReturnFlag);

        return insertReturnFlag;
    }

    private void saveOnClick(View v) {
        if (saveRecord() == -1) {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            // 返回活动结果
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newRecord", newRecord);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }


}
