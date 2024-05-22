package com.example.eventmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanager.data.EventDatabaseHelper;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity {

    private EditText titleEditText, locationEditText, descriptionEditText;
    private Button dateButton, timeButton, saveButton;
    private Calendar eventCalendar;
    private EventDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        titleEditText = findViewById(R.id.edit_text_title);
        locationEditText = findViewById(R.id.edit_text_location);
        descriptionEditText = findViewById(R.id.edit_text_description);
        dateButton = findViewById(R.id.button_date);
        timeButton = findViewById(R.id.button_time);
        saveButton = findViewById(R.id.button_save);

        eventCalendar = Calendar.getInstance();
        dbHelper = new EventDatabaseHelper(this);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        eventCalendar.set(Calendar.YEAR, year);
                        eventCalendar.set(Calendar.MONTH, month);
                        eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButtonText();
                    }
                },
                eventCalendar.get(Calendar.YEAR), eventCalendar.get(Calendar.MONTH), eventCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        eventCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        eventCalendar.set(Calendar.MINUTE, minute);
                        updateTimeButtonText();
                    }
                },
                eventCalendar.get(Calendar.HOUR_OF_DAY), eventCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void updateDateButtonText() {
        dateButton.setText(android.text.format.DateFormat.getDateFormat(this).format(eventCalendar.getTime()));
    }

    private void updateTimeButtonText() {
        timeButton.setText(android.text.format.DateFormat.getTimeFormat(this).format(eventCalendar.getTime()));
    }

    private void saveEvent() {
        String title = titleEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty() || location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(EventDatabaseHelper.COLUMN_TITLE, title);
        values.put(EventDatabaseHelper.COLUMN_DATETIME, eventCalendar.getTimeInMillis());
        values.put(EventDatabaseHelper.COLUMN_LOCATION, location);
        values.put(EventDatabaseHelper.COLUMN_DESCRIPTION, description);

        long newRowId = dbHelper.insertEvent(values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error saving event", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
