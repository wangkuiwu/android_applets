package com.skw.eventtest;

import android.view.MotionEvent;

public class Utils {

    public static String getActionName(MotionEvent event) {
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            return "DOWN";
        } else if (action == MotionEvent.ACTION_MOVE) {
            return "MOVE";
        } else if (action == MotionEvent.ACTION_UP) {
            return "UP";
        } else if (action == MotionEvent.ACTION_CANCEL) {
            return "CANCEL";
        } else {
            return "NULL";
        }
    }
}
