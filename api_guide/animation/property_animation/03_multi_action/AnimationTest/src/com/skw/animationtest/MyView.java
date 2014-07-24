package com.skw.animationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;

/**
 * 自定义"圆"
 */
public class MyView extends View {

    private float mX;
    private float mY;
    // 圆的渐变起始颜色
    private int mColorStart;
    // 圆的渐变结束颜色
    private int mColorEnd;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mX = 0;
        mY = 0;

        int red    = (int) (Math.random() * 255);
        int green  = (int) (Math.random() * 255);
        int blue   = (int) (Math.random() * 255);
        mColorStart = 0xff000000 | red << 16 | green << 8 | blue;
        mColorEnd   = 0xff000000 | (red / 4) << 16 | (green / 4) << 8 | (blue / 4) ;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 获取半径
        int radius = getWidth()/2;
        // 创建"圆"
        OvalShape circle = new OvalShape();
        circle.resize(2*radius, 2*radius);
        // 获取"圆的drawable对象"
        ShapeDrawable drawable = new ShapeDrawable(circle);
        // 参数：x坐标，y坐标，半径，渐变起始颜色，渐变结束颜色，模式
        RadialGradient gradient = new RadialGradient((float)radius, (float)radius, (float)radius,
                mColorStart, mColorEnd, Shader.TileMode.CLAMP);
        Paint paint = drawable.getPaint();
        paint.setShader(gradient);
        // 在画布上绘制图形
        canvas.translate(getX(), getY());
        drawable.draw(canvas);
    }

    public void setX(float x) {
        mX = x;
    }

    public float getX() {
        return mX;
    }

    public void setY(float y) {
        mY = y;
    }

    public float getY() {
        return mY;
    }

}
