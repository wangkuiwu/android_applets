package com.skw.viewtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

public class ViewTest extends Activity 
    implements View.OnClickListener, VerticalTabView.OnTabChangedListener {

    private TextView mText1, mText2, mText3;
    private VerticalTabView mTab;
    private Button mUp;
    private Button mDown;
    private Button mJump;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mUp= (Button)findViewById(R.id.upBtn);
        mUp.setOnClickListener(this);
        mDown= (Button)findViewById(R.id.downBtn);
        mDown.setOnClickListener(this);
        mJump= (Button)findViewById(R.id.jumpBtn);
        mJump.setOnClickListener(this);

        mTab = (VerticalTabView)findViewById(R.id.myViewGroup);
        mTab.setOnTabChangeListener(this);

        // init();
        // Button mTip = (Button)findViewById(R.id.tip);
        // mTip.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         showTip();
        //     }
        // });
    }

    private void init() {
        Context context = this.getApplicationContext();

        mText1 = new TextView(context);
        // mText1.setText("A");
        mText1.setSingleLine(false);
        mText1.setBackgroundColor(0x88ff0000);
        mTab.addTab(mText1);

        mText2 = new TextView(context);
        mText1.setSingleLine(false);
        // mText2.setText("B");
        mText2.setBackgroundColor(0x8800ff00);
        mTab.addTab(mText2);

        mText3 = new TextView(context);
        mText3.setSingleLine(false);
        // mText3.setText("C");
        mText3.setBackgroundColor(0x880000ff);
        mTab.addTab(mText3);
    }

    private void showTip() {
        Toast.makeText(this, "hi, sky", 0).show();
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.upBtn:
                mTab.scrollUp();
                break;
            case R.id.downBtn:
                mTab.scrollDown();
                break;
            case R.id.jumpBtn:
                mTab.setCurrentTab(0);
                break;
        }
    }

    @Override
    public void onTabChanged(int index) {
        Toast.makeText(this, "current Tab is: "+index, 0).show();
    }
}
