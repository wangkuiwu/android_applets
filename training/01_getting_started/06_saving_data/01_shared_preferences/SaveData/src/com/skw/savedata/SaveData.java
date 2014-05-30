package com.skw.savedata;

import android.app.Activity;   
import android.os.Bundle;   
import android.util.Log; 
import android.content.SharedPreferences; 
import android.content.res.Resources; 
import java.util.Map; 
import java.util.Map.Entry; 
import java.util.Iterator; 
   
public class SaveData extends Activity {
     
    private static final String TAG = "##SaveData##"; 
 
    private static final String SP_FILE_NAME = "sp_test";  
    private static final String SP_CHI_NAME  = "chi_name"; 
    private static final String SP_ENG_NAME  = "eng_name"; 
    private static final String SP_AGE       = "age"; 
    private static final String SP_CHINESE   = "chinese"; 
    
    // SharedPreferences对象    
    private SharedPreferences sp = null;
    // SharedPreferences编辑器
    private SharedPreferences.Editor editor = null; 
 
    private SharedPreferences.OnSharedPreferenceChangeListener listener =  
        new SharedPreferences.OnSharedPreferenceChangeListener() { 
            public void onSharedPreferenceChanged(SharedPreferences sp, String key) { 
                Log.d(TAG, "onSharedPreferenceChanged:key="+key); 
                if (SP_CHI_NAME.equals(key))  
                    Log.d(TAG, "onSharedPreferenceChanged:value="+sp.getString(key, null)); 
            }
    }; 
         
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main); 
         
        sp = getSharedPreferences(SP_FILE_NAME, MODE_PRIVATE); 
        // 监听 
        sp.registerOnSharedPreferenceChangeListener(listener); 
 
        // 编辑并保存数据 
        editor = sp.edit(); 
        editor.putString(SP_CHI_NAME, "kitty"); 
        editor.putString(SP_ENG_NAME, "nick01"); 
        editor.putString(SP_ENG_NAME, "nick02"); 
        editor.putInt(SP_AGE, 11); 
        editor.putBoolean(SP_CHINESE, true); 
        editor.commit();
 
        // 测试 
        Log.d(TAG, "chi_name="+sp.getString(SP_CHI_NAME, null)); 
        Log.d(TAG, "eng_name="+sp.getString(SP_ENG_NAME, null)); 
        Log.d(TAG, "contains(other_name)="+ sp.contains("other_name")); 
        Log.d(TAG, "other_name="+sp.getString("other_name", "none")); 
 
        // 测试getAll() 接口 
        Map<?,?> map = (Map<?,?>)sp.getAll(); 
        Log.d(TAG, "map.size()="+map.size()); 
        Iterator iterator=map.entrySet().iterator();     
        while(iterator.hasNext()){           
            Map.Entry<?, ?> entry= (Entry<?, ?>) iterator.next();     
            Log.d(TAG, "key:"+entry.getKey()+", value:"+entry.getValue());       
        } 
    }
 
    @Override 
    public void onDestroy() { 
        super.onDestroy(); 
        if (sp!=null) 
            sp.unregisterOnSharedPreferenceChangeListener(listener); 
    } 
}
