package com.skw.animationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.widget.LinearLayout;
import android.util.Log;

import java.util.ArrayList;

/**
 * Property Animation动画的基本使用方法
 */
public class AnimationTest extends Activity {

    private MyView view1;
    private MyView view2;
    private MyView view3;
    private MyView view4;
    private MyView view5;
    private LinearLayout mContainer;
    private ObjectAnimator mAnimator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mContainer = (LinearLayout) findViewById(R.id.container);
        view1 = (MyView) findViewById(R.id.ball1);
        view2 = (MyView) findViewById(R.id.ball2);
        view3 = (MyView) findViewById(R.id.ball3);
        view4 = (MyView) findViewById(R.id.ball4);
        view5 = (MyView) findViewById(R.id.ball5);
    }

    /**
     * 动画
     */
    private void startAnimation() {

        // ==== view1的动画 ==== (弹跳)
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view1, "y", 0f, (float)(view1.getHeight() - view1.getWidth()));
        anim1.setDuration(1200);
        anim1.setInterpolator(new BounceInterpolator());
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view1.invalidate();
            }
        });


        // ==== view2的动画 ==== (加速下降+变透明)
        // 方法一： 使用PropertyValuesHolder实现(推荐)
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(
                "y", 0f, (float)(view2.getHeight() - view2.getWidth()));
        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat(
                "alpha", 1.0f, 0f);
        ObjectAnimator anim2 = ObjectAnimator.ofPropertyValuesHolder(view2, pvhY, pvhAlpha);
        anim2.setDuration(500);
        anim2.setInterpolator(new AccelerateInterpolator());
        anim2.setRepeatCount(1);
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view2.invalidate();
            }
        });


        // ==== view3的动画 ==== (加速下降+变透明)
        // 方法二： 使用ObjectAnimator叠加
        ObjectAnimator anim3Y = ObjectAnimator.ofFloat(view3, "y", 0f, (float)(view3.getHeight() - view3.getWidth()));
        anim3Y.setDuration(500);
        anim3Y.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view3.invalidate();
            }
        });
        ObjectAnimator anim3Alpha = ObjectAnimator.ofFloat(view3, "alpha", 1f, 0f);
        anim3Alpha.setDuration(500);
        // 将"下降动画"和"上升动画"关联
        AnimatorSet anim3 = new AnimatorSet();
        anim3.playTogether(anim3Y, anim3Alpha);// 串行


        // 方法三：用ViewPropertyAnimator(只有View才支持ViewPropertyAnimator)
        // myView.animate().x(50f).y(100f);


        // ==== view4的动画 ==== (利用"关键帧"实现曲线运动)
        PropertyValuesHolder anim4Y = PropertyValuesHolder.ofFloat(
                "y", 0f, (float)(view2.getHeight() - view2.getWidth()));
        float x = view2.getX();
        // 三个关键帧
        Keyframe kf0 = Keyframe.ofFloat(0f, x);
        Keyframe kf1 = Keyframe.ofFloat(.5f, x + 20f);
        Keyframe kf2 = Keyframe.ofFloat(1f, x);
        PropertyValuesHolder anim4X = PropertyValuesHolder.ofKeyframe(
                "x", kf0, kf1, kf2);
        ObjectAnimator anim4 = ObjectAnimator.ofPropertyValuesHolder(view4, anim4Y, anim4X);
        anim4.setDuration(1000);
        anim4.setInterpolator(new AccelerateInterpolator());
        anim4.setRepeatCount(1);
        anim4.setRepeatMode(ValueAnimator.REVERSE);
        anim4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //view4.invalidate();
                mContainer.invalidate();
            }
        });


        // ==== view5的动画 ==== (变为透明)
        // view5继承于View，View自带setAlpha(float)方法
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(view5, "Alpha", 1, 0);
        anim5.setDuration(500);
        anim5.setRepeatCount(3);
        anim5.setRepeatMode(ValueAnimator.REVERSE);
        anim5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view5.invalidate();
            }
        });


        // ==== 总的动画 ==== 
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(anim1, anim2, anim3);// 并行
        animSet.playSequentially(anim3, anim4, anim5);// 串行
        animSet.start();
    }


    public void actionClick(View view) {
        startAnimation();
    }
}
