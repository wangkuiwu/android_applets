package com.skw.viewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.MeasureSpec;
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

    private int color = 0;
    private String text="hello my view";
    private Paint mPaint;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Style.FILL);  
        mPaint.setTextSize(35.0f);  
        setPadding(20, 60, 0, 0);
    }

    @Override  
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw");
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
        //canvas.drawText(text, getPaddingTop(), getPaddingLeft(), mPaint);  
        canvas.drawText(text, getLeft(), getTop(), mPaint);  
        Log.d(TAG, "onDraw: padtop="+getPaddingTop()+", padleft="+getPaddingLeft());
        Log.d(TAG, "onDraw: top="+getTop()+", left="+getLeft());
    }
  
    public void changeColor() { //为了让外面调用  
        color = (color+1)%3;
    }


    /** 
     * 比onDraw先执行 
     *  
     * 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。 
     * 一个MeasureSpec由大小和模式组成 
     * 它有三种模式：UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小; 
     *              EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小； 
     *              AT_MOST(至多)，子元素至多达到指定大小的值。 
     *  
     * 　　它常用的三个函数： 　　 
     * 1.static int getMode(int measureSpec):根据提供的测量值(格式)提取模式(上述三个模式之一) 
     * 2.static int getSize(int measureSpec):根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小)  
     * 3.static int makeMeasureSpec(int size,int mode):根据提供的大小值和模式创建一个测量值(格式) 
     */  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        Log.d(TAG, "onMeasure: EXACTLY="+MeasureSpec.EXACTLY
                +", AT_MOST="+MeasureSpec.AT_MOST
                +", UPSPECIFIED="+MeasureSpec.UNSPECIFIED);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));  
    }  
  
    private int measureWidth(int measureSpec) {  
        int result = 0;  
        int specMode = MeasureSpec.getMode(measureSpec);  
        int specSize = MeasureSpec.getSize(measureSpec);  
  
        if (specMode == MeasureSpec.EXACTLY) {  
            result = specSize;  
        } else {  
            // Measure the text  
            result = (int) mPaint.measureText(text) + getPaddingLeft() + getPaddingRight();  
            if (specMode == MeasureSpec.AT_MOST) {  
                result = Math.min(result, specSize);
            }  
        }  
        Log.d(TAG, "measureWidth: specMode="+specMode+", specSize="+specSize+", result="+result);
  
        return result;  
    }  
  
    private int measureHeight(int measureSpec) {  
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
  
        int mAscent = (int) mPaint.ascent();  
        Log.d(TAG, "measureHeight: specMode="+specMode+", specSize="+specSize);
        if (specMode == MeasureSpec.EXACTLY) {  
            result = specSize;  
        } else {  
            // Measure the text (beware: ascent is a negative number)  
            result = (int) (-mAscent + mPaint.descent()) + getPaddingTop() + getPaddingBottom();  
            if (specMode == MeasureSpec.AT_MOST) {  
                // Respect AT_MOST value if that was what is called for by  
                // measureSpec  
                result = Math.min(result, specSize);  
            }  
        }  
        //result = specSize;  
        Log.d(TAG, "measureHeight: mAscent="+mAscent+", mDescent="+mPaint.descent()+", result="+result);
        return result;  
    }
}
