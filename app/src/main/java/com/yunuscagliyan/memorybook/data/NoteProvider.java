package com.yunuscagliyan.memorybook.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NoteProvider extends ContentProvider {

    SQLiteDatabase db;

    static final String CONTENT_AUTHIRITY="com.yunuscagliyan.memorybook.data.noteprovider";
    static final String PATH_NOTES="notes";

    static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHIRITY);
    public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NOTES);
    //content://com.yunuscagliyan.memorybook.data.noteprovider/notes

    static final UriMatcher matcher;
    static {
        matcher=new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHIRITY,PATH_NOTES,1);
    }

    /**
     * DATABASE AND TABLE
     */
    private final static String DATABASE_NAME="notes.db";
    private final static int DATABASE_VERSION=6;
    public final static String TABLE_NAME="notes";
    public final static String COLUMN_NOTE_ID="id";
    public final static String COLUMN_NOTE_CONTENT="note_content";
    public final static String COLUMN_NOTE_INSERTION_TIME="note_insertion_time";
    public final static String COLUMN_NOTE_DATE="note_date";
    public final static String COLUMN_NOTE_COMPLETE="note_complete";

    private final static String CREATE_NOTES_TABLE="CREATE TABLE "+TABLE_NAME
            +"("+COLUMN_NOTE_ID+" INTEGER Primary Key AUTOINCREMENT,"
            +COLUMN_NOTE_CONTENT+" TEXT NOT NULL,"
            +COLUMN_NOTE_INSERTION_TIME+" INTEGER,"
            +COLUMN_NOTE_DATE+" INTEGER,"
            +COLUMN_NOTE_COMPLETE+" INTEGER DEFAULT 0);";


    @Override
    public boolean onCreate() {
        DatabaseHelper helper=new DatabaseHelper(getContext());
        db=helper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@Nullable Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (matcher.match(uri)){
            case 1:
                cursor=db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                if(cursor!=null){
                    return cursor;
                }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (matcher.match(uri)){
            case 1:
                Long insertedRowId=db.insert(TABLE_NAME,null,values);
                if(insertedRowId>0){
                    Uri _uri= ContentUris.withAppendedId(CONTENT_URI,insertedRowId);
                    return _uri;
                }
                break;

        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int affectedRowCount=0;
        switch (matcher.match(uri)){
            case 1:
                affectedRowCount=db.delete(TABLE_NAME,selection,selectionArgs);
        }
        return affectedRowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int affectedRowCount=0;
        switch (matcher.match(uri)){
            case 1:
                affectedRowCount=db.update(TABLE_NAME,values,selection,selectionArgs);
        }
        return affectedRowCount;
    }

    private class DatabaseHelper extends SQLiteOpenHelper{



        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NOTES_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);

        }
    }
}
