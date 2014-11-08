package com.skw.velocitytest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.util.Log;

public class VelocityTest extends Activity {
    private static final String TAG = "##skywang-Velocity";
    private TextView mInfo;

    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;

    private int mPointerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInfo = new TextView(this);
        mInfo.setLines(4);
        mInfo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        mInfo.setTextColor(Color.WHITE);

        setContentView(mInfo);

        mVelocityTracker = VelocityTracker.obtain();
        mMaxVelocity = ViewConfiguration.get(this).getMaximumFlingVelocity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //求第一个触点的id， 此时可能有多个触点，但至少一个
                mPointerId = event.getPointerId(0);

                mVelocityTracker.addMovement(event);
                Log.d(TAG, "Down");
                break;

            case MotionEvent.ACTION_MOVE:
            {
                //求瞬时速度
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = mVelocityTracker.getXVelocity(mPointerId);
                final float velocityY = mVelocityTracker.getYVelocity(mPointerId);
                recodeInfo(velocityX, velocityY);
                Log.d(TAG, "Move: velocityX="+velocityX+", velocityY="+velocityY);
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = mVelocityTracker.getXVelocity(mPointerId);
                final float velocityY = mVelocityTracker.getYVelocity(mPointerId);
                Log.d(TAG, "Up: velocityX="+velocityX+", velocityY="+velocityY);
                break;
            }

            case MotionEvent.ACTION_CANCEL:
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseVelocityTracker();
    }

    private void releaseVelocityTracker() {
        if(null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 记录当前速度
     *
     * @param velocityX x轴速度
     * @param velocityY y轴速度
     */
    private void recodeInfo(final float velocityX, final float velocityY) {
        final String info = String.format("velX=%f, velY=%f", velocityX, velocityY);
        mInfo.setText(info);
    }
}

