package com.skw.eventtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 默认情况下Activity, ViewGroup, View对事件的处理情况
 *
 *
 * 一、DOWN事件的处理流程(所有函数皆返回false)
 *         Activity                    ViewGroup                     View
 *         1.dispatchTouchEvent
 *                                     2. dispatchTouchEvent
 *                                     3. onInterceptTouchEvent
 *                                                                   4. dispatchTouchEvent
 *                                                                   5. onTouchEvent
 *                                     6. onTouchEvent
 *         7.onTouchEvent
 *
 * 二、MOVE事件的处理流程(所有函数皆返回false)
 *         Activity                    ViewGroup                     View
 *         1.dispatchTouchEvent
 *         2.onTouchEvent
 *
 * 三、UP事件的处理流程(所有函数皆返回false)
 *         Activity                    ViewGroup                     View
 *         1.dispatchTouchEvent
 *         2.onTouchEvent
 *
 */
public class MyActivity extends Activity {
    private static final String TAG = "##skywang-MyActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
        boolean ret = super.onTouchEvent(event);
        Log.d(TAG, "onTouchEvent( end ) :"+actionName+", ret="+ret);
        return ret;
    }
}
