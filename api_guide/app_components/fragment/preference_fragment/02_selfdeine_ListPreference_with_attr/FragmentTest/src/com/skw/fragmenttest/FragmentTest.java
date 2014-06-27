package com.skw.fragmenttest;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class FragmentTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 获取FragmentManager
        FragmentManager fragmentManager = getFragmentManager();
        // 获取FragmentTransaction        
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            
        PrefsFragment fragment = new PrefsFragment();
        // 将fragment添加到容器frag_example中
        fragmentTransaction.add(R.id.prefs, fragment);
        fragmentTransaction.commit();
    }
}
