package com.skw.providertest;

import android.app.Activity;
import android.os.Bundle;  
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.ContentResolver;
import android.content.ContentUris;  
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;  
import android.database.sqlite.SQLiteDatabase.CursorFactory;  
import android.net.Uri;  
import android.util.Log;

public class ProviderTest extends Activity 
    implements View.OnClickListener {

	private static final String TAG = "##ProviderTest##";

    // 数据库的属性，与MyProvider的表格属性一致
    public static final String NAME      = "name";
    public static final String BIRTH_DAY = "birthday";
    public static final String EMAIL     = "email";
    public static final String GENDER    = "gender";
    // 数据库的URI
	public static final Uri CONTENT_URI = Uri.parse("content://com.skw.myprovider/table01");

	private	ContentResolver mContentResolver = null;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        ((Button)findViewById(R.id.insert)).setOnClickListener(this);
        ((Button)findViewById(R.id.deleteFirst)).setOnClickListener(this);
        ((Button)findViewById(R.id.deleteKate)).setOnClickListener(this);
        ((Button)findViewById(R.id.deleteAll)).setOnClickListener(this);
        ((Button)findViewById(R.id.update)).setOnClickListener(this);
        ((Button)findViewById(R.id.show)).setOnClickListener(this);

		// 删除第一行，然后全部打印出来
		mContentResolver = getContentResolver();

	}


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.insert:
                // 添加
                insert("Jimmy", "20020201", "Jimmy20020201@126.com", 1);
                insert("Kate",  "20030104", "kate20030104@126.com", 0);
                insert("Li Lei", "20021124", "lilei20101124@126.com", 1);
                insert("Lucy", "20010624", "lucy20101124@126.com", 0);
                break;
            case R.id.deleteFirst:
                ContentUris cus = new ContentUris();
                Uri uri = cus.withAppendedId(CONTENT_URI, 1);
                Log.d(TAG, "delete uri="+uri);
                mContentResolver.delete(uri, null, null);
                break;
            case R.id.deleteKate:
                // 删除“username=Kate”的行，然后全部打印出来
                mContentResolver.delete(CONTENT_URI, NAME+"=?", new String[]{"Kate"});
                break;
            case R.id.deleteAll:
                // 删除全部的行，然后全部打印出来
                deleteAll() ;
                break;
            case R.id.update:
                // 更新第1个值，然后全部打印出来
                updateItem() ;
                break;
            case R.id.show:
                // 打印全部的值
                printAll() ;
                break;
            default:
                // 查找第2个值
                //querySecondItem() ;
                break;
        }
    }

	/*
	 * 通过ContentResolver,将值插入到MyProvider中
	 */
	private void insert(String name, String date, String email, int gender) {

		ContentResolver cr = getContentResolver();

	   	ContentValues cv = new ContentValues();
		cv.put(NAME, name);
		cv.put(BIRTH_DAY, date);
		cv.put(EMAIL, email);
		cv.put(GENDER, gender);
		Uri uri = cr.insert(CONTENT_URI, cv);
		Log.d(TAG, "insert uri="+uri);
	}

	private void updateItem() {
		ContentResolver cr = getContentResolver();

		ContentUris cus = new ContentUris();
		Uri uri = cus.withAppendedId(CONTENT_URI, 1);

	   	ContentValues cv = new ContentValues();
		cv.put(NAME, "update_name");
		cv.put(BIRTH_DAY, "update_date");
		cv.put(EMAIL, "update_email");
		cv.put(GENDER, 1);
		cr.update(uri, cv, null, null);
	}

	/*
	 * 通过ContentResolver,将MyProvider中的值全部删除
	 */
	private void deleteAll() {

		Log.d(TAG, "delete all value!");
		ContentResolver cr = getContentResolver();
		cr.delete(CONTENT_URI, null, null);
	}

	private void querySecondItem() {
		ContentResolver cr = getContentResolver();
		ContentUris cus = new ContentUris();
		Uri uri = cus.withAppendedId(CONTENT_URI, 2);
		String[] proj = new String[] { NAME, BIRTH_DAY, EMAIL, GENDER};
		Cursor cursor = cr.query(uri, proj, null, null, null);
		int index = 0;
		while (cursor.moveToNext()) {
			Log.d(TAG, "querySecondItem--"+index+"--"
					+", email=" + cursor.getString(cursor.getColumnIndex(EMAIL))
					+", username=" + cursor.getString(cursor.getColumnIndex(NAME))
					+", date=" + cursor.getString(cursor.getColumnIndex(BIRTH_DAY))
					+", gender=" + cursor.getInt(cursor.getColumnIndex(GENDER)));
			index++;
		}
	}

	private void printAll() {
		//通过contentResolver进行查找
		ContentResolver cr = getContentResolver();

		Log.d(TAG, "print all value!");
		// query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
		// 返回的列
		String[] proj = new String[] { NAME, BIRTH_DAY, EMAIL, GENDER};
		Cursor cursor = cr.query(
			CONTENT_URI, proj, null, null, null);
		int index = 0;
		while (cursor.moveToNext()) {
			Log.d(TAG, "printAll--"+index+"--"
					+", email=" + cursor.getString(cursor.getColumnIndex(EMAIL))
					+", username=" + cursor.getString(cursor.getColumnIndex(NAME))
					+", date=" + cursor.getString(cursor.getColumnIndex(BIRTH_DAY))
					+", gender=" + cursor.getString(cursor.getColumnIndex(GENDER)));
			index++;
	   	}
	   	startManagingCursor(cursor);  //查找后关闭游标
	}
}
