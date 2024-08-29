package com.huojieren.healthdiary.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.huojieren.healthdiary.R;
import com.huojieren.healthdiary.database.HealthDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddDietActivity extends AppCompatActivity {

    private EditText dietInput;
    private EditText dateInput;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diet);

        dietInput = findViewById(R.id.diet_input);
        dateInput = findViewById(R.id.date_input);
        dbHelper = new HealthDatabaseHelper(this);

        // 将当前日期设为默认值
        setCurrentDate();

        // 点击日期输入框时弹出选择框
        dateInput.setOnClickListener(v -> showDatePickerDialog());

        findViewById(R.id.save_diet_button).setOnClickListener(v -> {
            saveDietData();
            finish();
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
                (view, year1, month1, dayOfMonth1) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, month1, dayOfMonth1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String selectedDateString = sdf.format(selectedDate.getTime());
                    dateInput.setText(selectedDateString);
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void saveDietData() {
        String dietDetails = dietInput.getText().toString();
        String dietDate = dateInput.getText().toString();  // Get selected date
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", dietDate);
        values.put("description", dietDetails);
        db.insert("diet", null, values);
    }
}
