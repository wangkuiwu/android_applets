package com.skw.sharefile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class ShareFile extends Activity 
    implements View.OnClickListener {
    private static final String TAG = "##ShareFile##";

    private Intent mRequestFileIntent;

    private Button mSelect;
    private ImageView mHead;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mHead = (ImageView) findViewById(R.id.iv_head);

        Button mSelect = (Button) findViewById(R.id.bt_select);
        mSelect.setOnClickListener(this);

        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/jpg");
    }

    @Override 
    public void onClick(View v) {
        startActivityForResult(mRequestFileIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        if (resultCode != RESULT_OK) {
            Log.d(TAG, "result is not ok!");
            return ;
        } 
        
        Uri uri = intent.getData();
        try {
            ParcelFileDescriptor mInputPFD = getContentResolver().openFileDescriptor(uri, "r");
            if (mInputPFD != null) {
                FileDescriptor fd = mInputPFD.getFileDescriptor();
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd);
                mHead.setImageBitmap(bitmap);
                Log.d(TAG, "get bitmap success!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "File not found.");
            return;
        }

        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();

        String name = returnCursor.getString(nameIndex);
        long size = returnCursor.getLong(sizeIndex);

        Log.d(TAG, "name="+name+", size="+size);
    }
}
