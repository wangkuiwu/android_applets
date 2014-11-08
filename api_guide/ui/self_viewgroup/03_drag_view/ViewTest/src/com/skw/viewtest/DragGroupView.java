package com.skw.viewtest;

import android.content.Context;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.util.AttributeSet;
import android.util.Log;

public class DragGroupView extends ViewGroup {
    private final static String TAG = "##skywang-DragGroupView";

    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
    // 触摸状态
    private int mTouchState = TOUCH_STATE_REST;
    // 最小滑动距离
    private int mTouchSlop;

    private float mLastMotionX;
    private float mLastMotionY;

    public DragGroupView(Context context) {
        this(context, null);
    }

    public DragGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGroupView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //初始化一个最小滑动距离
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 计算控件的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件DragGroupView的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
        case MeasureSpec.AT_MOST:
        case MeasureSpec.EXACTLY:
            result = widthSize;
            break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
        case MeasureSpec.AT_MOST:
        case MeasureSpec.EXACTLY:
            result = heightSize;
            break;
        }
        return result;
    }

    /**
     * 覆写onLayout，其目的是为了指定视图的显示位置，方法执行的前后顺序是在onMeasure之后，因为视图肯定是只有知道大小的情况下，
     * 才能确定怎么摆放
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 记录总高度
        int mTotalHeight = 0;
        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();

            childView.layout(l, mTotalHeight, measuredWidth, mTotalHeight
                    + measureHeight);

            mTotalHeight += measureHeight;
        }
    }

    // 这个感觉没什么作用 不管true还是false 都是会执行onTouchEvent的 因为子view里面onTouchEvent返回false了
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

        final int action = event.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }   

        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
        case MotionEvent.ACTION_MOVE:
        {
            final int xDiff = (int) Math.abs(mLastMotionX - x); 
            Log.e(TAG, "onInterceptTouchEvent Move: xDiff="+xDiff+", mTouchSlop="+mTouchSlop);
            //超过了最小滑动距离
            if (xDiff > mTouchSlop) {
                mTouchState = TOUCH_STATE_SCROLLING;
                Log.e(TAG, "onInterceptTouchEvent Move: set to SCROLLING");
            }   
            break;
        }

        case MotionEvent.ACTION_DOWN:
            Log.e(TAG, "onInterceptTouchEvent Down: ("+x+", "+y+")");
            mLastMotionX = x;
            mLastMotionY = y;
            // mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
            mTouchState = TOUCH_STATE_REST;

            break;

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            Log.e(TAG, "onInterceptTouchEvent up or cancel");
            mTouchState = TOUCH_STATE_REST;
            break;
        }   

        return mTouchState != TOUCH_STATE_REST;
    }   


        @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Remember where the motion event started
                mLastMotionX = x;
                Log.e(TAG, "onTouchEvent Down: ("+x+", "+y+")");
                break;
            case MotionEvent.ACTION_MOVE:
                // Log.e(TAG, "onTouchEvent Move: ("+x+", "+y+")");
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    int deltaX = (int) (mLastMotionX - x);
                    mLastMotionX = x;
                    scrollBy(deltaX, 0);
                    Log.e(TAG, "onTouchEvent Move: ("+x+", "+y+"), scrollBy("+deltaX+", 0)");
                } else {
                    mLastMotionX = x;
                    mTouchState = TOUCH_STATE_SCROLLING;
                    Log.e(TAG, "onTouchEvent Move: ("+x+", "+y+"), set to SCROLLING");
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent UP");
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
        }

        return true;
    }
}

