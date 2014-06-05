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
        jump();
    }

    private void jump() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Hahaha, you've been tricked!");
        intent.setType("text/plain");
        startActivity(intent);
    }   
}
