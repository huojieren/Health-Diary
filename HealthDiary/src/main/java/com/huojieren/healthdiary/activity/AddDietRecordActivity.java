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

import androidx.appcompat.app.AppCompatActivity;

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;
import com.huojieren.healthdiary.model.dietRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddDietRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private HealthDatabaseHelper dbHelper;
    private dietRecord newRecord;
    private SimpleDateFormat Format;
    private Calendar selectedDate;
    private TextView tv_diet_date;
    private Button btn_diet_date;
    private Button btn_save;
    //    private GridView gv_food_icon;
//    private List<String> iconList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diet_record);

        // 设置 emoji 和标题
        TextView tv_emoji = findViewById(R.id.tv_emoji);
        tv_emoji.setText("🍳");
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("记录一次饮食");

//        gv_food_icon = findViewById(R.id.gv_food_icon);
        tv_diet_date = findViewById(R.id.tv_diet_date);
        btn_diet_date = findViewById(R.id.btn_diet_date);
        btn_save = findViewById(R.id.btn_save);

        dbHelper = HealthDatabaseHelper.getInstance(this);
        Format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        // 给 GridView 设置适配器
//        ArrayAdapter<String> iconAdapter = new ArrayAdapter<>(
//                this,
//                R.layout.food_icon_item,
//                iconList
//        );
//        gv_food_icon.setAdapter(iconAdapter);

        selectedDate = Calendar.getInstance();
        btn_diet_date.setOnClickListener(v -> selectDate());
        btn_save.setOnClickListener(this::saveOnClick);
    }

    // 弹出日期和时间选择器
    private void selectDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // 日期选择器的监听器
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedDate.set(Calendar.YEAR, year);
        selectedDate.set(Calendar.MONTH, month);
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // 弹出时间选择框
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                selectedDate.get(Calendar.HOUR_OF_DAY), selectedDate.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    // 时间选择器的监听器
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // 设置 sleepTime 的时间
        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedDate.set(Calendar.MINUTE, minute);
        // 更新显示
        tv_diet_date.setText(Format.format(selectedDate.getTime()));
    }

    // 保存此项记录
    private long saveRecord() {
        String recordDate = Format.format(selectedDate.getTime());
        Log.d("AddDietRecordActivity", "recordTime:" + recordDate);

        ContentValues record = new ContentValues();
        record.put("diet_date", recordDate);

        newRecord = new dietRecord(recordDate);

        Log.d("AddSleepRecordActivity", "record to add:" + record);
        long insertReturnFlag = dbHelper.insertRecord("sleep", record);
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
