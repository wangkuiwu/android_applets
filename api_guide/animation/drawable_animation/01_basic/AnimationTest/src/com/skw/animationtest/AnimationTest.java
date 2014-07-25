package com.skw.animationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.CompoundButton;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;

/**
 * Drawable Animation动画的基本使用方法
 */
public class AnimationTest extends Activity 
    implements View.OnClickListener {

    private ImageView mImage;
    private AnimationDrawable mAnimation;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        CheckBox mRepeat = (CheckBox)findViewById(R.id.repeat);
        mRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        mAnimation.setOneShot(!isChecked);
            }
        }); 

        ((Button)findViewById(R.id.start)).setOnClickListener(this);
        ((Button)findViewById(R.id.stop)).setOnClickListener(this);

        mImage = (ImageView)findViewById(R.id.animation);
		mImage.setBackgroundResource(R.anim.anim_kof);
		mAnimation = (AnimationDrawable) mImage.getBackground();
	}

    @Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.start: {
            // 如果动画是oneShot模式，播放完之后，isRunning()仍然是true
			if(mAnimation.isRunning()){
			    mAnimation.stop();
			    mAnimation.start();
            } else {
			    mAnimation.start();
            }
		}
			break;
		case R.id.stop: {
			if(mAnimation.isRunning()){
                mAnimation.stop();
            }
		}
			break;
		default:
			break;
		}
    }
}
