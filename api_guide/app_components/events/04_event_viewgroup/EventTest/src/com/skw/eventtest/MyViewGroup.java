package com.skw.eventtest;

import android.content.Context;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.util.Log;

public class MyViewGroup extends LinearLayout {
    private static final String TAG = "##skywang-MyViewGroup";
    
    public MyViewGroup(Context context){
        super(context);
    }
    
    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        String actionName = Utils.getActionName(event);
        Log.d(TAG, "dispatchTouchEvent(start) :"+actionName);
        boolean ret = super.dispatchTouchEvent(event);
        Log.d(TAG, "dispatchTouchEvent( end ) :"+actionName+", ret="+ret);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String actionName = Utils.getActionName(event);
        Log.d(TAG, "onTouchEvent(start) :"+actionName);
        // boolean ret = super.onTouchEvent(event);
        boolean ret = true;
        Log.d(TAG, "onTouchEvent( end ) :"+actionName+", ret="+ret);
        return ret;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        String actionName = Utils.getActionName(event);
        Log.d(TAG, "onInterceptTouchEvent(start) :"+actionName);
        // boolean ret = super.onInterceptTouchEvent(event);
        boolean ret = true;
        Log.d(TAG, "onInterceptTouchEvent( end ) :"+actionName+", ret="+ret);
        return ret;
    }
}
