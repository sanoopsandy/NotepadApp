package com.example.sanoop.mynotepad.constants;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by sanoop on 7/19/2016.
 */
public class Constants {
    public static final String NOTES= "notes";

    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String UPDATED_AT = "modified_time";
    public static final String CREATED_AT = "created_time";
    public static final String[] COLUMNS = {
            Constants.ID,
            Constants.TITLE,
            Constants.CONTENT,
            Constants.CREATED_AT
    };
}
