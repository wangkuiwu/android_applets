package com.skw.fragmenttest;

import android.app.Activity;
import android.os.Bundle;

public class FragmentTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        getFragmentManager().beginTransaction().replace(android.R.id.content,  
                new PrefsFragment()).commit();  
    }
}
