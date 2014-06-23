package com.skw.viewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ViewTest extends Activity {

    private MyView myview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        myview = (MyView) findViewById(R.id.myview);
    }

    public void onRefresh(View view) {
        myview.changeColor();
        myview.invalidate();
    }
}
