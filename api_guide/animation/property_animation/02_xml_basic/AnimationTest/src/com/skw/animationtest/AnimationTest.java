package com.skw.animationtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.widget.LinearLayout;
import android.util.Log;

import java.util.ArrayList;

/**
 * 通过xml定义Property Anition的测试程序
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
        Context context = this.getApplicationContext();

        // ==== view1的动画 ==== (加速下降)
        ObjectAnimator anim1 = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.anim.view01);
        anim1.setTarget(view1);
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view1.invalidate();
            }
        });


        // ==== view2的动画 ==== (加速下降)
        // 创建ValueAnimator动画：需要自己在onAnimationUpdate中实现变化。
        ValueAnimator anim2 = (ValueAnimator) AnimatorInflater.loadAnimator(context, R.anim.view02);
        anim2.setTarget(view2);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view2.setY((Float) animation.getAnimatedValue());
                view2.invalidate();
            }
        });


        // ==== view3的动画 ==== (先下降后上升)
        // 下降动画
        ObjectAnimator anim3 = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.anim.view03);
        anim3.setTarget(view3);
        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view3.invalidate();
            }
        });


        // ==== view4的动画 ==== (先下降后上升)
        AnimatorSet anim4 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.anim.view04);
        anim4.setTarget(view4);
        ArrayList<Animator> list = anim4.getChildAnimations();
        for (Animator item:list) {
            ((ObjectAnimator)item).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view4.invalidate();
                }   
            }); 
        }   


        // ==== view5的动画 ==== (变为透明)
        // view5继承于View，View自带setAlpha(float)方法
        ObjectAnimator anim5 = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.anim.view05);
        anim5.setTarget(view5);
        anim5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 更新view1
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
