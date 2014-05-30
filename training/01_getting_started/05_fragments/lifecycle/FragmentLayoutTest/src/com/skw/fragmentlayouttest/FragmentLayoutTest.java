package com.skw.fragmentlayouttest;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentLayoutTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // 获取FragmentManager
        FragmentManager fragmentManager = getFragmentManager();
        // 获取FragmentTransaction        
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        // 获取ExampleFragment
        ExampleFragment fragment = new ExampleFragment();
        // 将fragment添加到容器frag_example中
        fragmentTransaction.add(R.id.frag_example, fragment);
        fragmentTransaction.commit();
    }
}
