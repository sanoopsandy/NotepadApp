package com.example.sanoop.mynotepad.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sanoop.mynotepad.constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanoop on 7/19/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simple_note_app.db";
    private static final int DATABASE_VERSION = 1;
    private HashMap hp;
    SQLiteDatabase db;

    private static final String CREATE_TABLE_NOTE = "create table "
            + Constants.NOTES
            + "("
            + Constants.ID + " integer primary key autoincrement, "
            + Constants.TITLE + " text not null, "
            + Constants.CONTENT + " text not null, "
            + Constants.DATE + " text" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.NOTES);
        onCreate(db);
    }

    public Cursor fetchAll() {
        db = this.getReadableDatabase();
        Cursor mCursor = db.query(Constants.NOTES, new String[] {
                Constants.ID,
                Constants.TITLE,
                Constants.CONTENT,
                Constants.DATE
        }, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public boolean insertNotes(String title, String date, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, title);
        contentValues.put(Constants.DATE, date);
        contentValues.put(Constants.CONTENT, content);
        db.insert(Constants.NOTES, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor z = db.rawQuery("select * from " + Constants.NOTES + " where _id=" + id
                + "", null);
        return z;
    }
    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, Constants.NOTES);
        return numRows;
    }
    public boolean updateNotes(Integer id, String title, String date,
                               String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, title);
        contentValues.put(Constants.DATE, date);
        contentValues.put(Constants.CONTENT, content);
        db.update(Constants.NOTES, contentValues, "_id = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }
    public Integer deleteNotes(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Constants.NOTES, "_id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList getAll() {
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + Constants.NOTES, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(Constants.ID)));
            array_list.add(res.getString(res.getColumnIndex(Constants.CONTENT)));
            array_list.add(res.getString(res.getColumnIndex(Constants.DATE)));
            array_list.add(res.getString(res.getColumnIndex(Constants.TITLE)));
            res.moveToNext();
        }
        return array_list;
    }
}