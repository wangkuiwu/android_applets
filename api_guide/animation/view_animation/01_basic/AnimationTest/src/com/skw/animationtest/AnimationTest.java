package com.skw.animationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.util.Log;

/**
 * View Animation动画的基本使用方法(透明度变化/旋转/缩放/移动)
 */
public class AnimationTest extends Activity 
    implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
			
        ((Button)findViewById(R.id.alpha)).setOnClickListener(this);
        ((Button)findViewById(R.id.rotate)).setOnClickListener(this);
        ((Button)findViewById(R.id.scale)).setOnClickListener(this);
        ((Button)findViewById(R.id.translate)).setOnClickListener(this);
        ((Button)findViewById(R.id.merge)).setOnClickListener(this);
	}

    @Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.alpha: {
	        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
			view.startAnimation(anim);
		}
			break;
		case R.id.rotate: {
	        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
			view.startAnimation(anim);
		}
			break;

		case R.id.scale: {
	        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
			view.startAnimation(anim);
		}
			break;
		case R.id.translate: {
	        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
			view.startAnimation(anim);
		}
			break;
		case R.id.merge: {
	        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_merge);
			view.startAnimation(anim);
		}
			break;
		default:
			break;
		}
    }
}
