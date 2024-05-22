package com.example.eventmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventmanager.data.EventDatabaseHelper;

import java.util.Calendar;

public class EventDetailsActivity extends AppCompatActivity {

    private long eventId;
    private EventDatabaseHelper dbHelper;
    private TextView titleTextView, dateTextView, timeTextView, locationTextView, descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        titleTextView = findViewById(R.id.text_view_title);
        dateTextView = findViewById(R.id.text_view_date);
        timeTextView = findViewById(R.id.text_view_time);
        locationTextView = findViewById(R.id.text_view_location);
        descriptionTextView = findViewById(R.id.text_view_description);

        dbHelper = new EventDatabaseHelper(this);

        eventId = getIntent().getLongExtra("EVENT_ID", -1);
        if (eventId != -1) {
            loadEventDetails(eventId);
        }
    }

    private void loadEventDetails(long eventId) {
        Cursor cursor = dbHelper.getEventById(eventId);
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabaseHelper.COLUMN_TITLE));
            long datetime = cursor.getLong(cursor.getColumnIndexOrThrow(EventDatabaseHelper.COLUMN_DATETIME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabaseHelper.COLUMN_LOCATION));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabaseHelper.COLUMN_DESCRIPTION));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(datetime);

            String date = DateFormat.getDateFormat(this).format(calendar.getTime());
            String time = DateFormat.getTimeFormat(this).format(calendar.getTime());

            titleTextView.setText(title);
            dateTextView.setText(date);
            timeTextView.setText(time);
            locationTextView.setText(location);
            descriptionTextView.setText(description);

            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, NewEventActivity.class);
            intent.putExtra("EVENT_ID", eventId);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete) {
            confirmDeleteEvent();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void confirmDeleteEvent() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.deleteEvent(eventId);
                        Toast.makeText(EventDetailsActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
