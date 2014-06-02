package com.skw.feedreader;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.skw.feedreader.FeedReaderContract;
import com.skw.feedreader.FeedReaderContract.FeedEntry;

public class FeedReader extends Activity {

    private static final String TAG = "##FeedReader##";

    private FeedReaderDbHelper mDbHelper;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
        FeedEntry._ID + " INTEGER PRIMARY KEY," +
        FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
        FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
        FeedEntry.COLUMN_NAME_CONTENT + TEXT_TYPE +
        " )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        openDatabase() ;
        testWriteDatabase(1, "first", "unknow test");
        testWriteDatabase(2, "second", "hello kitty");
        testReadDatabase() ;

        //testDeleteDatabase() ;
        //testReadDatabase() ;

        testUpdateDatabase() ;
        testReadDatabase() ;
    }

    private void openDatabase() {
        Log.d(TAG, "open databse");
        mDbHelper = new FeedReaderDbHelper(getApplicationContext());
    }

    private void testWriteDatabase(int id, String title, String content) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, id);
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedEntry.COLUMN_NAME_CONTENT, content);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                 FeedEntry.TABLE_NAME,
                 FeedEntry.COLUMN_NAME_NULLABLE,
                 values);
    }

    private void testReadDatabase() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            FeedEntry._ID,
            FeedEntry.COLUMN_NAME_ENTRY_ID,
            FeedEntry.COLUMN_NAME_TITLE,
            FeedEntry.COLUMN_NAME_CONTENT,
            };
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + "=?";
        String[] selectionArgs = new String[] {"1"};

        Cursor c = db.query(
            FeedEntry.TABLE_NAME,  // 被查询的表格
            projection,            // 返回属性：查询结果表中包含的"属性"
            selection,             // 查找条件(1)：相当于where的前半段
            selectionArgs,         // 查找条件(2)：相当于where的后半段
            null,                  // don't group the rows
            null,                  // don't filter by row groups
            null                   // don't sort 
            );

        Log.d(TAG, "read Database");
        while(c.moveToNext()) {
            // 
            int id = c.getInt(c.getColumnIndex(FeedEntry._ID)); 
            int entryid = c.getInt(c.getColumnIndex(FeedEntry.COLUMN_NAME_ENTRY_ID)); 
            String title = c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_TITLE)); 
            String content = c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_CONTENT)); 
            Log.d(TAG, "id="+id+", entryid="+entryid+", title="+title+", content="+content);
        }
    }

    private void testDeleteDatabase() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "1" };
        // Issue SQL statement.
        db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void testUpdateDatabase() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, "text updated!");

        // Which row to update, based on the ID
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(1) };

        int count = db.update(
            FeedEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs);
    }


    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d(TAG, "FeedReaderDbHelper");
        }   

        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "FeedReaderDbHelper: onCreate");
            db.execSQL(SQL_CREATE_ENTRIES);
        }   
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "FeedReaderDbHelper: onUpgrade");
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }   
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }   
    }   
}
