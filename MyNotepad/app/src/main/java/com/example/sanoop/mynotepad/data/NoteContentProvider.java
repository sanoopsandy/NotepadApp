package com.example.sanoop.mynotepad.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.sanoop.mynotepad.constants.Constants;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by sanoop on 7/19/2016.
 */
public class NoteContentProvider extends ContentProvider {
    private DatabaseHelper dbHelper;
    private static final String BASE_PATH_NOTE = "notes";
    private static final int SINGLE_NOTE = 100;
    private static final int ALL_NOTES = 101;
    private static final String AUTHORITY = "com.example.sanoop.mynotepad.data.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_NOTE);
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_NOTE, ALL_NOTES);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_NOTE + "/#", SINGLE_NOTE);

    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return false;
    }

    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> request = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> available = new HashSet<String>(Arrays.asList(Constants.COLUMNS));
            if (!available.containsAll(request)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);

        int type = URI_MATCHER.match(uri);
        switch (type){
            case SINGLE_NOTE:
                //there is not to do if the query is for the table
                break;
            case ALL_NOTES:
                queryBuilder.appendWhere(Constants.ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id;
        switch (type){
            case ALL_NOTES:
                id = db.insert(Constants.NOTES, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH_NOTE + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type) {
            case ALL_NOTES:
                affectedRows = db.delete(Constants.NOTES, selection, selectionArgs);
                break;

            case SINGLE_NOTE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.delete(Constants.NOTES, Constants.ID + "=" + id, null);
                } else {
                    affectedRows = db.delete(Constants.NOTES, Constants.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type) {
            case ALL_NOTES:
                affectedRows = db.update(Constants.NOTES, values, selection, selectionArgs);
                break;

            case SINGLE_NOTE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.update(Constants.NOTES, values, Constants.ID + "=" + id, null);
                } else {
                    affectedRows = db.update(Constants.NOTES, values, Constants.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }
}