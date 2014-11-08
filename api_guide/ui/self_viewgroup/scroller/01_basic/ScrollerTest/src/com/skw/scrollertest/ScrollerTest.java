package com.skw.scrollertest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class ScrollerTest extends Activity {
    private static final String TAG = "##skywang-TestScrollerActivity";
    // LinearLayout lay1, lay2, lay0;
    LinearLayout mLayoutContainer;
    LinearLayout mLayout1, mLayout2;
    private Scroller mScroller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScroller = new Scroller(this);
        // Layout Container
        mLayoutContainer = new ContentLinearLayout(this);
        mLayoutContainer.setOrientation(LinearLayout.VERTICAL);
        // LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
        //         LinearLayout.LayoutParams.FILL_PARENT,
        //         LinearLayout.LayoutParams.FILL_PARENT);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(400,  LinearLayout.LayoutParams.FILL_PARENT);
        setContentView(mLayoutContainer, param);

        // Layout1
        mLayout1 = new MyLinearLayout(this);
        mLayout1.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        param1.weight = 1;
        mLayoutContainer.addView(mLayout1, param1);
        // button
        MyButton btn1 = new MyButton(this);
        btn1.setText("btn in layout1");
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始滚动，设置初始位置--》结束位置 & 持续时间
                mScroller.startScroll(0, 0, -30, -30, 500);
            }
        });
        mLayout1.addView(btn1);

        // Layout2
        mLayout2 = new MyLinearLayout(this);
        mLayout2.setBackgroundColor(this.getResources().getColor(android.R.color.white));
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        param2.weight = 1;
        mLayoutContainer.addView(mLayout2, param2);
        // button
        MyButton btn2 = new MyButton(this);
        btn2.setText("btn in layout2");
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // mScroller.startScroll(20, 20, -50, -50, 500);
            }
        });
        mLayout2.addView(btn2);
    }

    class MyButton extends Button {
        public MyButton(Context ctx) {
            super(ctx);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.d(TAG, this.toString() + " onDraw------");
        }
    }

    class MyLinearLayout extends LinearLayout {
        public MyLinearLayout(Context ctx) {
            super(ctx);
        }

        @Override
        public void computeScroll() {
            Log.d(TAG, this.toString() + " computeScroll-----------");
            if (mScroller.computeScrollOffset())//如果mScroller没有调用startScroll，这里将会返回false。
            {
                //因为调用computeScroll函数的是MyLinearLayout实例，
                //所以调用scrollTo移动的将是该实例的孩子，也就是MyButton实例
                scrollTo(mScroller.getCurrX(), 0);
                Log.d(TAG, "getCurrX = " + mScroller.getCurrX());

                //继续让系统重绘
                // getChildAt(0).invalidate();
            }
        }
    }

    class ContentLinearLayout extends LinearLayout {
        public ContentLinearLayout(Context ctx) {
            super(ctx);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            Log.d(TAG, "contentview dispatchDraw");
            super.dispatchDraw(canvas);
        }

        @Override
        public void computeScroll() {
            //
            // if (mScroller.computeScrollOffset())
            //     scrollTo(mScroller.getCurrX(), 0);
        }
    }
}
