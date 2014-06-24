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
import android.util.Log;

public class MyView extends View {
    private static final String TAG="##MyView##";

    private Paint mPaint;
    private String mText = "drawText";

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        // FILL填充, STROKE描边,FILL_AND_STROKE填充和描边
        mPaint.setStyle(Style.FILL);
        // 画一个矩形
        canvas.drawRect(new Rect(10, 10, 140, 80), mPaint);

        mPaint.setColor(Color.GREEN);
        mPaint.setTextSize(26.0f);
        canvas.drawText(mText, 10, 60, mPaint);

        Log.d(TAG, "x="+getX()+", y="+getY());
        Log.d(TAG, "left="+getLeft()+", top="+getTop());
        Log.d(TAG, "padLeft="+getPaddingLeft()+", padTop="+getPaddingTop());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
