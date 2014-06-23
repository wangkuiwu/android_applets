package com.skw.viewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;

public class MyView extends View {

    private int color = 0;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override  
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint mPaint = new Paint();
        switch (color) {
        case 0:
            mPaint.setColor(Color.GREEN);
            break;
        case 1:
            mPaint.setColor(Color.RED);  
            break;
        case 2:
            mPaint.setColor(Color.BLUE);
            break;
  
        default:  
            break;  
        }  
        mPaint.setStyle(Style.FILL);  
        mPaint.setTextSize(35.0f);  
        canvas.drawText("hell my view", 10, 60, mPaint);  
    }
  
    public void changeColor() { //为了让外面调用  
        color = (color+1)%3;
    }
}
