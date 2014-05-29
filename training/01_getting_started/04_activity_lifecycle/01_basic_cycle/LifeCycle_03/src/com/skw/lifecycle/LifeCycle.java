package com.skw.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

public class LifeCycle extends Activity {

    private final static String TAG = "##LifeCycle##";

    static final String STATE_SCORE = "playerScore";
    static final String STATE_LEVEL = "playerLevel";

    private int mCurrentScore;
    private int mCurrentLevel;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.d(TAG, "onCreate: savedInstanceState="+savedInstanceState);
        if (savedInstanceState!=null) {
            mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
            mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
        } else {
            mCurrentScore = 0;
            mCurrentLevel = 1;
        }
        Log.d(TAG, "onCreate: mCurrentScore="+mCurrentScore+", mCurrentLevel="+mCurrentLevel);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // 保存数据
        savedInstanceState.putInt(STATE_SCORE, mCurrentScore);
        savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);
        
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState: mCurrentScore="+mCurrentScore+", mCurrentLevel="+mCurrentLevel);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // 还原数据
        super.onRestoreInstanceState(savedInstanceState);
       
        // Restore state members from saved instance
        mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
        mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
        Log.d(TAG, "onRestoreInstanceState: mCurrentScore="+mCurrentScore+", mCurrentLevel="+mCurrentLevel);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    // 按钮回调函数
    public void sendMessage(View view) {
        mCurrentScore = 60;
        mCurrentLevel = 10;
        Log.d(TAG, "onClick: mCurrentScore="+mCurrentScore+", mCurrentLevel="+mCurrentLevel);
    }
}
