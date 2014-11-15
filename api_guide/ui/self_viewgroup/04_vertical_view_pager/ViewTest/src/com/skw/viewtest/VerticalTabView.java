package com.skw.viewtest;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.VelocityTracker;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

public class VerticalTabView extends ViewGroup {
    private final static String TAG = "##skywang-VerticalTabView";

    private final static int SNAP_VELOCITY = 500;

    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
    // 触摸状态
    private int mTouchState = TOUCH_STATE_REST;

    private final static int SCROLL_REST = 0;
    private final static int SCROLL_UP   = 1;
    private final static int SCROLL_DOWN = 2;
    // 滚动状态
    private int mDirection = SCROLL_REST;

    // 最小滑动距离
    private int mTouchSlop;

    private float mLastMotionX;
    private float mLastMotionY;

    private int mIndex;
    private int mScrollTotal = 0;

    // indicator 
    private ImageView mIndicatorView;
    // indicator size
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    // indicator position
    private int mIndicatorPrevTop = 0;
    private int mIndicatorTop = 0;
    private int mIndicatorLeft = 0;

    // 滚动测试
    private Scroller mScroller;
    // 速度测量
    private VelocityTracker mVelocityTracker;

    // Tab页的中点在Y轴上的坐标
    private List<Integer> mTopBoundaries = new ArrayList<Integer>();
    private List<Integer> mBottomBoundaries = new ArrayList<Integer>();

    // 游标对应的Tab变化的监听函数
    private OnTabChangedListener mTabChangeListener;

    public VerticalTabView(Context context) {
        this(context, null);
    }

    public VerticalTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int screenWidthDip = dm.widthPixels;
        int screenHeightDip = dm.heightPixels;

        Log.d(TAG, "w="+screenWidthDip+", h="+screenHeightDip);
        //初始化一个最小滑动距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mScroller = new Scroller(context);

        mIndex = 0;
        // indicator
        mIndicatorWidth = 20;
        mIndicatorHeight = 20;
        mIndicatorView = new ImageView(context);
        mIndicatorView.setImageResource(R.drawable.triangle);
        addView(mIndicatorView, 0, new ViewGroup.LayoutParams(mIndicatorWidth, mIndicatorHeight));
    }

    public void addTab(View view) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        if (count < 1) {
            return ;
        }

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        // indicator view
        mIndicatorView.measure(mIndicatorWidth, mIndicatorHeight);

        // other views
        int childWidth  = measureWidth - mIndicatorWidth;
        int childHeight = measureHeight/(count-1);
        int residual = measureHeight%(count-1);

        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        for (int i=count-1; i>0; i--) {
            View child = getChildAt(i);
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            if (residual > 0) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight+1, MeasureSpec.EXACTLY);
                residual--;
            } else {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            }
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            // Log.d(TAG, "onMeasure child:"+i+", ("+childWidthMeasureSpec+"x"+childHeightMeasureSpec+")");
            // Log.d(TAG, "onMeasure child:"+i+", ("+childWidth+"x"+childHeight+")");
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(TAG, "onLayout: l="+l+", r="+r+", t="+t+", b="+b);

        mTopBoundaries.clear();
        mBottomBoundaries.clear();

        // 记录总高度
        int mTotalHeight = 0;
        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 1; i < childCount; i++) {
            View child = getChildAt(i);

            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = child.getMeasuredHeight();
            int measureWidth = child.getMeasuredWidth();
            // Log.d(TAG, "onLayout view:"+i+", ("+measureWidth+"x"+measureHeight+")");

            mTopBoundaries.add(mTotalHeight+measureHeight/2-mIndicatorHeight/2);
            mBottomBoundaries.add(mTotalHeight+measureHeight/2+mIndicatorHeight/2);
            child.layout(l+mIndicatorWidth, mTotalHeight, measureWidth+mIndicatorWidth, mTotalHeight
                    + measureHeight);
            Log.d(TAG, "onLayout view:"+i+", l="+child.getLeft()+", t="+child.getTop()
                    +", r="+child.getRight()+", b="+child.getBottom());

            mTotalHeight += measureHeight;
        }
        Log.d(TAG, "onLayout mTopBoundaries:"+mTopBoundaries);
        Log.d(TAG, "onLayout mBottomBoundaries:"+mBottomBoundaries);

        initIndicatorPosition();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /* TODO
        if (true) {
            return ;
        }
        if (isInEditMode()) {
            return;
        }

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(0xff45c01a);

        int count = getChildCount();
        for (int i=0; i<count; i++) {
            View view = getChildAt(i);
            int t=view.getTop();
            int b=view.getBottom();
            int l=view.getLeft();
            int r=view.getRight();
            int w=view.getWidth();
            int h=view.getHeight();
            Log.d(TAG, "o-view: "+i+", l="+l+", r="+r+", t="+t+", b="+b+" ("+w+", "+h+")");

            // canvas.drawRect(0, t+10, mIndicatorWidth, b-10, mPaint);
            drawTriangle(canvas, mPaint, (t+b)/2);
        }
        // final int height = getHeight();
        // mPaint.setColor(0xff000000);
        // canvas.drawRect(0, 0, 40, 40, mPaint);

        // draw underline
        // rectPaint.setColor(underlineColor);
        // canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
        */
    }

    private void drawTriangle(Canvas canvas, Paint p, int middleY) {
        Path path = new Path();  
        path.moveTo(mIndicatorWidth, middleY);
        path.lineTo(0, middleY-10);  
        path.lineTo(0, middleY+10);  
        path.close(); // 使这些点构成封闭的多边形  
        canvas.drawPath(path, p);
        Log.d(TAG, "draw triangle: middleY="+middleY);
    }

    /**
     * 根据mIndex的值初始化游标的位置。
     *
     * 例如，mIndex=0，则游标指向第一个Tab页。
     */
    private void initIndicatorPosition() {
        mIndicatorLeft = 0;
        mIndicatorTop = mTopBoundaries.get(mIndex);
        mIndicatorPrevTop = mIndicatorTop;

        mIndicatorView.layout(mIndicatorLeft, mIndicatorTop, mIndicatorLeft+mIndicatorWidth, mIndicatorTop+mIndicatorHeight );
    }

    /**
     * 滚动游标
     *
     * @param
     *   deltaY 在y轴上滚动的相对距离。 表示向上
     */
    private void scrollIndicatorBy(int deltaY) {
        // 滚动的方向：上或者下
        mDirection = (deltaY<0) ? SCROLL_UP : SCROLL_DOWN;

        if (mDirection==SCROLL_UP && arriveParrentTop()) {
            Log.d(TAG, "arrive parent's top");
            return ;
        }
        if (mDirection==SCROLL_DOWN && arriveParrentBottom()) {
            Log.d(TAG, "arrive parent's bottom");
            return ;
        }

        mIndicatorTop += deltaY;
        mIndicatorView.layout(mIndicatorLeft, mIndicatorTop, mIndicatorLeft+mIndicatorWidth, mIndicatorTop+mIndicatorHeight );
        Log.d(TAG, "scrollIndicatorBy: deltaY="+deltaY+", mIndicatorTop="+mIndicatorTop);
    }

    /**
     * 游标 是否到达父窗口的顶部
     */
    private boolean arriveParrentTop() {
        if (mIndicatorTop<=0) {
            return true;
        }

        return false;
    }

    /**
     * 游标 是否到达父窗口的底部
     */
    private boolean arriveParrentBottom() {
        if (mIndicatorTop+mIndicatorHeight >= getHeight()) {
            return true;
        }

        return false;
    }

    /**
     * 游标中点在y轴上的坐标
     */
    private int verticalMiddle() {
        return mIndicatorTop+mIndicatorHeight/2;
    }

    /**
     * 游标中点在View(view的序号是index)的顶部以下
     */
    private boolean isIndicatorMiddleBelowViewTop(int index) {
        if (index>0 && (index<getChildCount())) {
            int viewTop = getChildAt(index).getTop();
            return verticalMiddle() > viewTop;
        }

        return false;
    }

    /**
     * 游标中点在View(view的序号是index)的底部以上
     */
    private boolean isIndicatorMiddleAboveViewBottom(int index) {
        if (index>0 && (index<getChildCount())) {
            int viewBottom = getChildAt(index).getBottom();
            Log.d(TAG, "middle=:"+verticalMiddle()+", upViewBottom="+viewBottom);
            return verticalMiddle() < viewBottom;
        }

        return false;
    }


    /**
     * 滑动到目的地
     */
    private void snapToDestination() {
        int prevTop = mIndicatorPrevTop;
        int currTop = mIndicatorTop;
        Log.d(TAG, "snapToDestination: prevTop="+prevTop+", currTop="+currTop
                + ", mIndex="+mIndex+", mIndicatorTop="+mIndicatorTop);

        // scroll down
        if (prevTop < currTop) {
            if ( isIndicatorMiddleBelowViewTop(mIndex+2) ) {
                scrollDown();
            } else {
                scrollBack();
            }
        }

        // scroll up
        if (prevTop > currTop) {
            if ( isIndicatorMiddleAboveViewBottom(mIndex) ) {
                scrollUp();
            } else {
                scrollBack();
            }
        }
    }

    /**
     * 将游标返回到"开始滚动之前的原始位置"
     */
    private void scrollBack() {
        int currTop = mIndicatorTop;
        int nextTop = mTopBoundaries.get(mIndex);

        if (currTop==nextTop) {
            Log.d(TAG, "no need to scrollBack");
            return ;
        }
        Log.d(TAG, "scrollBack: currTop="+currTop+", nextTop="+nextTop+", mScrollTotal="+mScrollTotal);
        mScrollTotal = nextTop - currTop;
        mScroller.startScroll(0, 0, 0, mScrollTotal);

        invalidate();
    }

    /**
     * 获取Tab页的数目
     */
    private int getTabSize() {
        return mTopBoundaries.size();
    }

    /**
     * 将游标滚动到上一个Tab的中点处
     */
    public void scrollUp() {
        setCurrentTab(mIndex-1);
    }

    /**
     * 将游标滚动到下一个Tab的中点处
     */
    public void scrollDown() {
        setCurrentTab(mIndex+1);
    }

    /**
     * 设置当前的Tab页
     */
    public void setCurrentTab(int index) {

        if (index<0 || index==mIndex || index>(getTabSize()-1)) {
            Log.d(TAG, "setCurrentTab return!");
            return ;
        }

        int currTop = mIndicatorTop;
        int nextTop = mTopBoundaries.get(index);

        // 更新标签索引
        mIndex = index;
        // 回调函数
        if (mTabChangeListener!=null) {
            mTabChangeListener.onTabChanged(index);
        }
        // 滚动的总高度
        mScrollTotal = nextTop - currTop;
        // 开始滚动
        mScroller.startScroll(0, 0, 0, mScrollTotal);
        // 刷新
        invalidate();
    }

    /**
     * 获取当前的Tab也
     */
    public int getCurrentTab() {
        return mIndex;
    }

    @Override
    public void computeScroll() {
        Log.e(TAG, "computeScroll");

        // 调用mScroller.startScroll()之后，mScroller就开始计算滚动偏移；
        // 此时，computeScrollOffset()才会返回true。当计算完成时，computeScrollOffset()返回false。
        if (mScroller.computeScrollOffset()) {
            int scrollY = mScroller.getCurrY();
            int currTop = mIndicatorTop + scrollY;

            // 更新游标
            mIndicatorView.layout(mIndicatorLeft, currTop, mIndicatorLeft+mIndicatorWidth, currTop+mIndicatorHeight);
            postInvalidate();
            Log.d(TAG, "computeScroll: mIndicatorTop"+mIndicatorTop+", currTop="+currTop);

            // Scroller 计算完毕时，再更新mIndicatorTop
            if (scrollY==mScrollTotal) {
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                mIndicatorTop = currTop;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }   

        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
        case MotionEvent.ACTION_MOVE:
        {
            final int yDiff = (int) Math.abs(mLastMotionX - y); 
            Log.e(TAG, "onInterceptTouchEvent Move: yDiff="+yDiff+", mTouchSlop="+mTouchSlop);
            //超过了最小滑动距离
            if (yDiff > mTouchSlop) {
                Log.d(TAG, "onInterceptTouchEvent Move: set to SCROLLING prevTop="+mIndicatorPrevTop+", currTop="+mIndicatorTop);
                mIndicatorPrevTop = mIndicatorTop;
                mTouchState = TOUCH_STATE_SCROLLING;
            }   
            break;
        }

        case MotionEvent.ACTION_DOWN:
            boolean scrollFinish = mScroller.isFinished();
            Log.e(TAG, "onInterceptTouchEvent Down: ("+x+", "+y+")"+", scrollFinish="+scrollFinish);
            mLastMotionX = x;
            mLastMotionY = y;
            mTouchState = scrollFinish ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;

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

        if (mVelocityTracker == null) {
            Log.e(TAG, "onTouchEvent start-------** VelocityTracker.obtain");
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Remember where the motion event started
                mLastMotionX = x;
                mLastMotionY = y;
                Log.e(TAG, "onTouchEvent Down: ("+x+", "+y+")");

                // 如果屏幕的动画还没结束，就按下了，那么就结束该动画
                if(mScroller != null){
                    if(!mScroller.isFinished()){
                        mScroller.abortAnimation();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    int deltaY = (int) (mLastMotionY - y);
                    mLastMotionY = y;

                    Log.d(TAG, "onTouchEvent Move: ("+x+", "+y+"), scrollBy("+deltaY+", 0)");
                    scrollIndicatorBy(deltaY);
                    // scrollBy(0, deltaY);
                } else {
                    mLastMotionY = y;
                    mIndicatorPrevTop = mIndicatorTop;
                    mTouchState = TOUCH_STATE_SCROLLING;
                    Log.d(TAG, "onTouchEvent Move: ("+x+", "+y+"), set to SCROLLING");
                }

                break;
            case MotionEvent.ACTION_UP:
            {
                final VelocityTracker velocityTracker = mVelocityTracker  ;
                // 设置速度的计算单位是"秒"
                velocityTracker.computeCurrentVelocity(1000);

                int velY = (int) velocityTracker.getYVelocity() ;

                Log.e(TAG , "onTouchEvent UP: ---velY---" + velY);

                //滑动速率达到了一个标准(快速向右滑屏，返回上一个屏幕) 马上进行切屏处理
                if (velY > SNAP_VELOCITY) {
                    // Fling enough to move left
                    Log.e(TAG, "snap up");
                    if (mIndex>0) {
                        scrollUp();
                    } else {
                        scrollBack();
                    }
                }
                // 快速向上滑屏，返回下一个tab)
                else if(velY < -SNAP_VELOCITY){
                    Log.e(TAG, "snap down");
                    if (mIndex<getTabSize()-1) {
                        scrollDown();
                    } else {
                        scrollBack();
                    }
                    // scrollDown();
                }
                //以上为快速移动的 ，强制切换屏幕
                else{
                    snapToDestination();
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                mTouchState = TOUCH_STATE_REST;
                mDirection = SCROLL_REST;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                mDirection = SCROLL_REST;
        }

        return true;
    }


    public void setOnTabChangeListener(OnTabChangedListener listener) {
        this.mTabChangeListener = listener;
    }

    public interface OnTabChangedListener {
        public void onTabChanged(int index);
    }
}
