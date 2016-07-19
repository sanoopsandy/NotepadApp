package com.example.sanoop.mynotepad.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sanoop.mynotepad.constants.Constants;

/**
 * Created by sanoop on 7/19/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simple_note_app.db";
    private static final int DATABASE_VERSION = 1;



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

    private static final String CREATE_TABLE_NOTE = "create table "
            + Constants.NOTES
            + "("
            + Constants.ID + " integer primary key autoincrement, "
            + Constants.TITLE + " text not null, "
            + Constants.CONTENT + " text not null, "
            + Constants.UPDATED_AT + " integer not null, "
            + Constants.CREATED_AT + " integer not null" + ")";
}