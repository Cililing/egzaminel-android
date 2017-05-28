package com.example.przemek.egzaminel.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class _DataProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.przemek.temporaryAppName";
    static final String URL = "content://" + PROVIDER_NAME + "/";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, DatabaseHelper.TABlE_GROUPS, 1);
        uriMatcher.addURI(PROVIDER_NAME, DatabaseHelper.TABLE_EXAMS, 2);
        uriMatcher.addURI(PROVIDER_NAME, DatabaseHelper.TABLE_TERMS, 3);
    }


    Context context;
    SQLiteDatabase database;
    DatabaseHelper dbHelper;


    @Override
    public boolean onCreate() {
        context = getContext();
        dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getWritableDatabase();

        return !(database == null);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int operationID = uriMatcher.match(uri);
        long rowID = -1;
        switch (operationID) {
            case 1: {
                rowID = database.insert(DatabaseHelper.TABlE_GROUPS, "", values);
                break;
            }
            case 2: {
                rowID = database.insert(DatabaseHelper.TABLE_EXAMS, "", values);
                break;
            }
            case 3: {
                rowID = database.insert(DatabaseHelper.TABLE_TERMS, "", values);
                break;
            }
        }

        if (rowID > 0) {
            Uri uriWithRowID = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(uriWithRowID, null);
            return uriWithRowID;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
