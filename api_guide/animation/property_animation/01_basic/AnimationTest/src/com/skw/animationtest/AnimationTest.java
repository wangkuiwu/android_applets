package com.skw.animationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

        // ==== view1的动画 ==== (弹跳效果)
        // 创建ObjectAnimator动画。view1中必须有setY方法
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view1, "y", 0f, (float)(view1.getHeight() - view1.getWidth()));
        // 总的显示时间
        anim1.setDuration(1200);
        // 变化模式(弹跳)
        anim1.setInterpolator(new BounceInterpolator());
        // 监听：每次变化时的回调函数
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 更新view1
                view1.invalidate();
            }
        });


        // ==== view2的动画 ==== (加速下降)
        // 创建ValueAnimator动画：需要自己在onAnimationUpdate中实现变化。
        ValueAnimator anim2 = ValueAnimator.ofFloat(0f, (float)(view2.getHeight() - view2.getWidth()));
        // 每帧的显示时间
        anim2.setDuration(500);
        // 变化模式(加速)
        anim2.setInterpolator(new AccelerateInterpolator());
        // 监听：每次变化时的回调函数
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 设置view2的纵坐标y
                view2.setY((Float) animation.getAnimatedValue());
                // 更新view2
                view2.invalidate();
            }
        });


        // ==== view3的动画 ==== (先下降后上升)
        // 下降动画
        ObjectAnimator anim3Down = ObjectAnimator.ofFloat(view3, "y", 0f, (float)(view3.getHeight() - view3.getWidth()));
        anim3Down.setDuration(500);
        anim3Down.setInterpolator(new AccelerateInterpolator());
        anim3Down.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view3.invalidate();
            }
        });

        // 上升降动画
        ObjectAnimator anim3Up = ObjectAnimator.ofFloat(view3, "y", (float)(view3.getHeight() - view3.getWidth()), 0f);
        anim3Up.setDuration(500);
        anim3Up.setInterpolator(new DecelerateInterpolator());
        anim3Up.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view3.invalidate();
            }
        });

        // 将"下降动画"和"上升动画"关联
        AnimatorSet anim3 = new AnimatorSet();
        anim3.playSequentially(anim3Down, anim3Up);// 串行


        // ==== view4的动画 ==== (先下降后上升)
        AnimatorSet anim4 = anim3.clone();
        anim4.setTarget(view4);
        // 重新anim4的监听函数
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
        ObjectAnimator anim5Hide = ObjectAnimator.ofFloat(view5, "Alpha", 1, 0);
        anim5Hide.setDuration(500);
        anim5Hide.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view5.invalidate();
            }
        });

        ObjectAnimator anim5Show = ObjectAnimator.ofFloat(view5, "Alpha", 0, 1);
        anim5Show.setDuration(500);
        anim5Show.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view5.invalidate();
            }
        });
        AnimatorSet anim5 = new AnimatorSet();
        anim5.playSequentially(anim5Hide, anim5Show);


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
