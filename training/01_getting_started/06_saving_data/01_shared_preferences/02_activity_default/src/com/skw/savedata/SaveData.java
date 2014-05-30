package com.skw.savedata;

import android.app.Activity;   
import android.os.Bundle;   
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log; 
   
public class SaveData extends Activity {
     
    private static final String TAG = "##SaveData##"; 
 
    static final String SP_FILE_NAME = "sp_test";  
    static final String SP_NAME    = "sp_name"; 
    static final String SP_AGE     = "sp_age"; 
    static final String SP_SINGLE  = "sp_single"; 
    
    // SharedPreferences对象    
    private SharedPreferences mPref = null;
    // SharedPreferences编辑器
    private SharedPreferences.Editor mEditor = null; 
 
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main); 
         
        // 获取SharedPreferences方法一：指定名称
        //mPref = getSharedPreferences(SP_FILE_NAME, MODE_PRIVATE); 
        // 获取SharedPreferences方法二：apk默认
        mPref = getPreferences(MODE_PRIVATE);
        mEditor = mPref.edit(); 

        writePref();
        readPref();
    }

    private void writePref() {
        mEditor.putString(SP_NAME, "jim");      // 名字
        mEditor.putInt(SP_AGE, 25);             // 年龄
        mEditor.putBoolean(SP_SINGLE, false);   // 婚否

        // 提交数据
        mEditor.commit();
    }

    private void readPref() {
        String name = mPref.getString(SP_NAME, null); 
        int age = mPref.getInt(SP_AGE, 1); 
        boolean single = mPref.getBoolean(SP_SINGLE, false); 
        Log.d(TAG, "mPref="+mPref+", mEditor="+mEditor);
        Log.d(TAG, "name="+name+", age="+age+", single="+single);
    }

    public void sendMessage(View view) {
        Log.d(TAG, "send msg");
        Intent intent = new Intent(this, Son.class);
        startActivity(intent);
        Log.d(TAG, "send msg over");
    }
 
    @Override 
    public void onDestroy() { 
        super.onDestroy(); 
    } 
}
