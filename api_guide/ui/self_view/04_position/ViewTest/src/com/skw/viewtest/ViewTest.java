package com.skw.viewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.util.Log;

public class ViewTest extends Activity 
    implements View.OnTouchListener {
    private static final String TAG="##ViewTest##";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int rawx = (int) event.getRawX();
                int rawy = (int) event.getRawY();
                Log.d(TAG, "x="+x+", y="+y+";  rawX="+rawx+", rawY="+rawy);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return false;
    }
}
