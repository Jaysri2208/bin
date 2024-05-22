package com.example.eventmanager;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.eventmanager.data.EventDatabaseHelper;

public class EventCursorAdapter extends CursorAdapter {

    public EventCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleTextView = view.findViewById(R.id.event_title);
        TextView dateTextView = view.findViewById(R.id.event_date);

        String title = cursor.getString(cursor.getColumnIndexOrThrow(EventDatabaseHelper.COLUMN_TITLE));
        long datetime = cursor.getLong(cursor.getColumnIndexOrThrow(EventDatabaseHelper.COLUMN_DATETIME));

        titleTextView.setText(title);
        dateTextView.setText(android.text.format.DateFormat.getDateFormat(context).format(datetime));
    }
}
