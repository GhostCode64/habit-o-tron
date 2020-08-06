package com.ghostcode.habit_o_tron;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ghostcode.habit_o_tron.DatabaseHelper.*;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertNewCompletionTime() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DATE, dateFormatter.format(date));
        contentValue.put(DatabaseHelper.TIME, timeFormatter.format(date));
        database.insert(TABLE_NAME, null, contentValue);
    }

    public ArrayList getAllPulses() {
        this.open();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = database.rawQuery( "select * from " + TABLE_NAME, null );
        res.moveToFirst();
        while(!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(DATE)) + " " + res.getString(res.getColumnIndex(TIME)));
            res.moveToNext();
        }
        this.close();
        return array_list;
    }

    public List<Entry> getAllPulsesForGraph(List<Entry> entries) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH");
        SimpleDateFormat timeHourFormatter = new SimpleDateFormat("HH");
        SimpleDateFormat timeMinuteFormatter = new SimpleDateFormat("HH");

        this.open();
        Cursor res = database.rawQuery( "select * from " + TABLE_NAME, null );
        res.moveToFirst();
        while (!res.isAfterLast()) {
            // turn your data into Entry objects
            float day = Float.parseFloat(res.getString(res.getColumnIndex(DatabaseHelper._ID)));
            Log.d("get pulses for graph", res.getString(res.getColumnIndex(TIME)));

            Date rawTime = new Date();

            try {
                rawTime = timeFormatter.parse(res.getString(res.getColumnIndex(TIME)));
            } catch (Exception ex){

            }
            float timeInHours = Integer.parseInt(timeHourFormatter.format(rawTime));
            float timeInMinutes = Integer.parseInt(timeMinuteFormatter.format(rawTime));

            float time = timeInHours + (timeInMinutes / 60);

            entries.add(new Entry(day, time));
            res.moveToNext();
        }
        this.close();
        return entries;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, name);
        contentValues.put(DatabaseHelper.TIME, desc);
        int i = database.update(TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }
}
