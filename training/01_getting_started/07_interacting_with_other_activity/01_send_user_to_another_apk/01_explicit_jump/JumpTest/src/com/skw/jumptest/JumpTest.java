package com.skw.jumptest;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ComponentName;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class JumpTest extends Activity 
    implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button mJump = (Button) findViewById(R.id.bt_jump);
        mJump.setOnClickListener(this);
    }

    @Override 
    public void onClick(View v) {
        jump2();
    }

    // 方法一
    private void jump1() {
        Intent intent = new Intent(this, DestinationActivity.class);
        startActivity(intent);
    }

    // 方法二
    private void jump2() {
        Intent intent = new Intent();
        ComponentName component = new ComponentName("com.skw.jumptest", "com.skw.jumptest.DestinationActivity");
        intent.setComponent(component);
        startActivity(intent);
    }
}
