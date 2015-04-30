package com.skw.loadertest;

import android.app.Dialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;

public class LoaderTest extends FragmentActivity 
    implements LoaderCallbacks<Cursor>, OnItemClickListener {
    private static final String TAG = "##LoaderTest##";

    private Bitmap mBitmap = null;
    private byte[] mContent = null;
    
    private ListView listView = null;
    private SimpleCursorAdapter simpleCursorAdapter = null;
    
    private static final String[] STORE_IMAGES = {
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(TAG, "onCreate");
        listView = (ListView)findViewById(android.R.id.list);
        simpleCursorAdapter = new SimpleCursorAdapter(
                this, 
                R.layout.list_item, 
                null, 
                STORE_IMAGES, 
                new int[] { R.id.item_title, R.id.item_value}
                );
        
        simpleCursorAdapter.setViewBinder(new ImageLocationBinder());
        listView.setAdapter(simpleCursorAdapter);
        // 注意此处是getSupportLoaderManager()，而不是getLoaderManager()方法。
        getSupportLoaderManager().initLoader(0, null, this);
        
        listView.setOnItemClickListener(this);
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        // 为了查看信息，需要用到CursorLoader。
        Log.d(TAG, "onCreateLoader");
        CursorLoader cursorLoader = new CursorLoader(
                this, 
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
                STORE_IMAGES, 
                null, 
                null, 
                null);
        return cursorLoader;
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onLoaderReset");
        simpleCursorAdapter.swapCursor(null);
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        // TODO Auto-generated method stub
        // 使用swapCursor()方法，以使旧的游标不被关闭．
        Log.d(TAG, "onLoadFinished");
        simpleCursorAdapter.swapCursor(cursor);
    }
    
    // 将图片的位置绑定到视图
    private class ImageLocationBinder implements ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (columnIndex == 1) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(column_index);
                ((TextView)view).setText("Location: "+path);
                return true;
            } 

            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(LoaderTest.this);
        // 以对话框形式显示图片
        dialog.setContentView(R.layout.image_show);
        dialog.setTitle("图片显示");

        ImageView ivImageShow = (ImageView) dialog.findViewById(R.id.ivImageShow);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                
                // 释放资源
                if(mBitmap != null){
                    mBitmap.recycle();
                }
            }
        });
        
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                  appendPath(Long.toString(id)).build();
        FileUtil file = new FileUtil();
        ContentResolver resolver = getContentResolver();
        
        // 从Uri中读取图片资源
        try {
            mContent = file.readInputStream(resolver.openInputStream(Uri.parse(uri.toString())));
            mBitmap = file.getBitmapFromBytes(mContent, null);
            ivImageShow.setImageBitmap(mBitmap);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        dialog.show();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        
        Log.d(TAG, "onDestroy");
        if(mBitmap != null){
            mBitmap.recycle();
        }
    }
}
