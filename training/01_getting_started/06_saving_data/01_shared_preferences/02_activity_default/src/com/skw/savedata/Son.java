package com.skw.savedata;

import android.app.Activity;   
import android.os.Bundle;   
import android.content.SharedPreferences;
import android.widget.TextView;
import android.util.Log; 
   
public class Son extends Activity {
     
    private static final String TAG = "##Son##"; 
 
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 

        TextView tv = new TextView(this);
        tv.setText("hello Son!");
        setContentView(tv); 
         
        testPref();
    }

    private void testPref() {
        SharedPreferences mPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPref.edit(); 

        mEditor.putString(SaveData.SP_NAME, "lily");    // 名字
        mEditor.putInt(SaveData.SP_AGE, 21);            // 年龄
        mEditor.putBoolean(SaveData.SP_SINGLE, true);   // 婚否
        // 提交数据
        mEditor.commit();

        String name = mPref.getString(SaveData.SP_NAME, null); 
        int age = mPref.getInt(SaveData.SP_AGE, 1); 
        boolean single = mPref.getBoolean(SaveData.SP_SINGLE, false); 
        Log.d(TAG, "mPref="+mPref+", mEditor="+mEditor);
        Log.d(TAG, "name="+name+", age="+age+", single="+single);
    }
 
    @Override 
    public void onDestroy() { 
        super.onDestroy(); 
    }
}
