package com.manning.androidhacks.hack002;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button btn1, btn2,btn3;
	private ViewStub viewStub;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//获取控件，绑定事件
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		viewStub = (ViewStub) findViewById(R.id.stub);
		viewStub.setOnInflateListener(inflateListener);
		btn1.setOnClickListener(click);
		btn2.setOnClickListener(click);
		btn3.setOnClickListener(click);
	}

	private OnInflateListener inflateListener=new OnInflateListener() {
		
		@Override
		public void onInflate(ViewStub stub, View inflated) {
			// inflaye ViewStub的时候显示
			Toast.makeText(MainActivity.this, "ViewStub is loaded!", Toast.LENGTH_SHORT).show();
		}
	};
	
	private View.OnClickListener click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn1:
				try {
					//如果没有被inflate过，使用inflate膨胀
					LinearLayout layout=(LinearLayout)viewStub.inflate();
					RatingBar bar=(RatingBar)layout.findViewById(R.id.ratingBar1);
					bar.setNumStars(4);
				} catch (Exception e) {
					//如果使用inflate膨胀报错，就说明已经被膨胀过了，使用setVisibility方法显示
					viewStub.setVisibility(View.VISIBLE);
				}				
				break;

			case R.id.btn2:
				//隐藏ViewStub
				viewStub.setVisibility(View.GONE);
				break;
			case R.id.btn3:
				//操作被inflate的控件，需要得到当前布局的对象
				//然后通过这个对象去找到被inflate的控件。
				//因为否则在这个示例中，会找到include标签引入的控件
				LinearLayout linearLayout=(LinearLayout)findViewById(R.id.inflatedStart);
				RatingBar rBar=(RatingBar)linearLayout.findViewById(R.id.ratingBar1);
				float numStart=rBar.getRating();
				numStart++;
				if(numStart>4)
				{
					numStart=0;
				}
				rBar.setRating(numStart);
				break;
			}

		}
	};
}
