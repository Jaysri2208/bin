package com.example.eventmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.eventmanager.data.EventDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private EventDatabaseHelper dbHelper;
    private ListView eventListView;
    private EventCursorAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new EventDatabaseHelper(this);
        eventListView = findViewById(R.id.event_list);

        loadEventList();

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);
                intent.putExtra("EVENT_ID", id);
                startActivity(intent);
            }
        });
    }

    private void loadEventList() {
        Cursor cursor = dbHelper.getAllEvents();
        eventAdapter = new EventCursorAdapter(this, cursor, 0);
        eventListView.setAdapter(eventAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_event) {
            startActivity(new Intent(this, NewEventActivity.class));
            return true;
        } else if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
