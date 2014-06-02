package com.skw.filetest;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTest extends Activity {

    private static final String TAG = "##FileTest##";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        testOpenFile();
        testJavaAPI();
        testTempFile();
        testDeleteFile();
    }

    private void testOpenFile() {
        String filename = "myfile";
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testDeleteFile() {
        String filename = "myfile";

        deleteFile(filename);
        Log.d(TAG, "file:"+filename+" is deleted!");
    }


    private void testTempFile() {
        String filename = "mytemp";
        String string = "temprory file!";
        FileOutputStream outputStream;

        try {
            File file = File.createTempFile(filename, null, getCacheDir());
            outputStream = new FileOutputStream(file);
            outputStream.write(string.getBytes());
            outputStream.close();

            Log.d(TAG, "file="+file);
            Log.d(TAG, "cacheDir="+getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
    }

    private void testJavaAPI() {
        String filename = "myjava";
        String string = "Hello kitty!";
        FileOutputStream outputStream;

        try {
            File file = new File(getFilesDir(), filename);
            outputStream = new FileOutputStream(file);
            outputStream.write(string.getBytes());
            outputStream.close();

            Log.d(TAG, "file="+file);
            Log.d(TAG, "filesDir="+getFilesDir());
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

}
