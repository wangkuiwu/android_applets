package com.skw.myprovider;

import android.net.Uri;  
import android.content.UriMatcher;  
import android.content.Context;
import android.content.ContentUris;  
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.ContentProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;  
import android.database.sqlite.SQLiteDatabase.CursorFactory;  
import android.util.Log;

import com.skw.myprovider.MyContract.Entry;

public class MyProvider extends ContentProvider{
	private static final String TAG = "##MyProvider##";
 
	private DBLiteHelper mDbHelper = null;

    // Uri的authority
    public static final String AUTHORITY = "com.skw.myprovider";
    // Uri的path
    public static final String PATH = "table01";
    // UriMatcher中URI对应的序号
    public static final int ITEM_ALL = 1;
    public static final int ITEM_ID  = 2;
    // Uri
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY +"/" + PATH);

    // 创建表格的SQL语句
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + Entry.TABLE_NAME + " (" +
        Entry._ID + " INTEGER PRIMARY KEY," +
        Entry.NAME + " TEXT NOT NULL, " +
        Entry.BIRTH_DAY + " TEXT, " +
        Entry.EMAIL + " TEXT, " +
        Entry.GENDER + " INTEGER " +
        " )";

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	static{
		URI_MATCHER.addURI(AUTHORITY, PATH, ITEM_ALL);
		URI_MATCHER.addURI(AUTHORITY, PATH+"/#", ITEM_ID);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new DBLiteHelper(this.getContext());
		Log.d(TAG, "open/create table");
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		int count = 0;
		switch (URI_MATCHER.match(uri)) {
		case ITEM_ALL:
			count = db.delete(Entry.TABLE_NAME, selection, selectionArgs);
			Log.d(TAG, "delete ITEM uri="+uri+", count="+count);
			break;
		case ITEM_ID:
			// 获取id列的值
			String id = uri.getPathSegments().get(1);
			count = db.delete(Entry.TABLE_NAME, Entry._ID+"=?", new String[]{id});
			Log.d(TAG, "delete ITEM_ID id="+id+", uri="+uri+", count="+count);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
		case ITEM_ALL:
			return "skw.myprovider.dir/table01";
		case ITEM_ID:
			return "skw.myprovider.item/table01";
		default:
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		if (URI_MATCHER.match(uri) != ITEM_ALL) {
			throw new IllegalArgumentException("Unknown URI"+uri);
		}

		long rowId = db.insert(Entry.TABLE_NAME, Entry._ID, values);
	   	if(rowId > 0) {
		   Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
		   getContext().getContentResolver().notifyChange(noteUri, null);
		   Log.d(TAG, "insert uri="+uri);
		   return noteUri;
	   	}

	   	throw new IllegalArgumentException("Unknown URI"+uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();                
		int count = 0;
		switch (URI_MATCHER.match(uri)) {
		case ITEM_ALL:
			count = db.update(Entry.TABLE_NAME, values, selection, selectionArgs);
		    Log.d(TAG, "update Item uri="+uri+", count="+count);
			break;
		case ITEM_ID:
			String id = uri.getPathSegments().get(1);
			count = db.update(Entry.TABLE_NAME, values, Entry._ID+"=?", new String[]{id});
		    Log.d(TAG, "update ITEM_ID uri="+uri+", count="+count);
			break;
		default:
			Log.d("!!!!!!", "Unknown URI"+uri);
			throw new IllegalArgumentException("Unknown URI"+uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
					String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();                
		Cursor c = null;
		switch (URI_MATCHER.match(uri)) {
		case ITEM_ALL:
			c = db.query(Entry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
			Log.d(TAG, "query ITEM uri="+uri);
			break;
		case ITEM_ID:
			// 获取id列的值
			String id = uri.getPathSegments().get(1);
			c = db.query(Entry.TABLE_NAME, projection, Entry._ID+"=?", new String[]{id}, null, null, null);
			Log.d(TAG, "query ITEM_ID id="+id+", uri="+uri);
			break;
		default:
			Log.d("!!!!!!", "Unknown URI"+uri);
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}


	private class DBLiteHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "MyProvider.db";

		public DBLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

		@Override
		public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}
