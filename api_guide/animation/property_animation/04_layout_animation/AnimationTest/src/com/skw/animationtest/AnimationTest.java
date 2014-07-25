package com.skw.animationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;

import java.util.ArrayList;

/**
 * Property Animation定义布局的添加/删除等动画
 */
public class AnimationTest extends Activity {

    private LinearLayout mDefaultLayout;
    private LinearLayout mSelfLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mDefaultLayout = (LinearLayout) findViewById(R.id.default_layout);
        mSelfLayout    = (LinearLayout) findViewById(R.id.self_layout);
    }

    /**
     * 系统默认的添加/删除视图的动画
     */
    public void defaultAction(View view) {
        Button button = new Button(AnimationTest.this);
        button.setText("Hello");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultLayout.removeView(view);
            }
        });
        mDefaultLayout.addView(button);
    }

    public void setupSelfAnimation() {
        LayoutTransition transition = new LayoutTransition();
        mSelfLayout.setLayoutTransition(transition);
        // 设置延迟时间
        transition.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
        transition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);

        // (添加)动画：APPEARING
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f);
        animIn.setDuration(transition.getDuration(LayoutTransition.APPEARING));
        transition.setAnimator(LayoutTransition.APPEARING, animIn);
        animIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationY(0f);
            }
        });

        // (删除)动画：DISAPPEARING
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f);
        animOut.setDuration(transition.getDuration(LayoutTransition.DISAPPEARING));
        transition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        animOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationX(0f);
            }
        });

        // 动画：CHANGE_APPEARING
        // Changing while Adding
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1); 
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1); 
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1); 
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1); 
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
        final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY);
        changeIn.setDuration(transition.getDuration(LayoutTransition.CHANGE_APPEARING));
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
        changeIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
            }   
        }); 

        // 动画：CHANGE_DISAPPEARING
        // Changing while Removing
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
        final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation);
        changeOut.setDuration(transition.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
        changeOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotation(0f);
            }
        });
    }

    /**
     * 自定义的添加/删除试图的动画
     */
    public void selfAction(View view) {
        setupSelfAnimation();

        Button button = new Button(AnimationTest.this);
        button.setText("World");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelfLayout.removeView(view);
            }
        });
        // 总是加在指定位置
        mSelfLayout.addView(button, 2);
    }
}
