package com.skw.sharefile;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class ShareFile extends Activity 
    implements View.OnClickListener {

    private Intent mRequestFileIntent;
    //private ParcelFileDescriptor mInputPFD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button mSelect = (Button) findViewById(R.id.bt_select);
        mSelect.setOnClickListener(this);

        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/jpg");
    }

    @Override 
    public void onClick(View v) {
        startActivityForResult(mRequestFileIntent, 0);
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            Uri uri = intent.getData();
            try {
                mInputPFD = getContentResolver().openFileDescriptor(uri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActivity", "File not found.");
                return;
            }
            FileDescriptor fd = mInputPFD.getFileDescriptor();
            // TODO

            Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            TextView nameView = (TextView) findViewById(R.id.filename_text);
            TextView sizeView = (TextView) findViewById(R.id.filesize_text);
            nameView.setText(returnCursor.getString(nameIndex));
            sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));

        }
    }
// */
}
