package com.skw.viewtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.util.Log;

public class PokeViewGroup extends ViewGroup {
    private static final String TAG = "##skywang-Poke";

    private int mHorizontalSpacing = 30;
    private int mVerticalSpacing = 30;

    public PokeViewGroup(Context context) {
        super(context);
    }

    public PokeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PokeViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        // int totalWidth = 0;
        // int totalHeight = 0;
        int width = getPaddingLeft();
        int height = getPaddingTop();
        // int hSpacing = 0;
        // int vSpacing = 0;
        for (int i=0; i<count; i++) {

            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);

            LayoutParams lp = (LayoutParams)view.getLayoutParams();
            // lp.x = width + (i==0 ? 0: mHorizontalSpacing);
            // lp.y = height + (i==0 ? 0 : mVerticalSpacing);
            lp.x = getPaddingLeft() + i*mHorizontalSpacing;
            lp.y = getPaddingTop() + i*mVerticalSpacing;

            width  = (lp.x + view.getMeasuredWidth());
            height = (lp.y + view.getMeasuredHeight());
            Log.d(TAG, "OnMeasure view-"+i+": x="+lp.x+", y="+lp.y+", w="+view.getMeasuredWidth()+", h="+view.getMeasuredHeight());
        }

        width  += getPaddingRight();
        height += getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), 
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 记录总高度
        int mTotalHeight = 0;
        // 遍历所有子视图
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);

            int measureHeight = view.getMeasuredHeight();
            int measuredWidth = view.getMeasuredWidth();

            LayoutParams lp = (LayoutParams)view.getLayoutParams();
            view.layout(lp.x, lp.y, lp.x+measuredWidth, lp.y+measureHeight);
            Log.d(TAG, "OnLayout view-"+i+": x="+lp.x+", y="+lp.y+", w="+view.getMeasuredWidth()+", h="+view.getMeasuredHeight());
        }
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }


    private static class LayoutParams extends ViewGroup.LayoutParams {
        int x;
        int y;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }
    }
}

