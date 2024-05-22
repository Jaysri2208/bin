package com.example.eventmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATETIME = "datetime"; // Combined datetime column
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DESCRIPTION = "description";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATETIME + " INTEGER, " + // Store datetime as INTEGER (timestamp)
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EVENTS, null, null, null, null, null, COLUMN_DATETIME + " ASC");
    }

    public Cursor getEventById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        return db.query(TABLE_EVENTS, null, selection, selectionArgs, null, null, null);
    }

    public void deleteEvent(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };
        db.delete(TABLE_EVENTS, whereClause, whereArgs);
    }

    public long insertEvent(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_EVENTS, null, values);
    }
}
