package com.skw.textviewtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TextViewTest extends Activity 
    implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.jump_text_watcher).setOnClickListener(this);
        findViewById(R.id.jump_marquee).setOnClickListener(this);
        findViewById(R.id.jump_spannable).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_text_watcher:
                startActivity(new Intent(TextViewTest.this, TextWatcherActivity.class));
                break;
            case R.id.jump_marquee:
                startActivity(new Intent(TextViewTest.this, MarqueeActivity.class));
                break;
            case R.id.jump_spannable:
                startActivity(new Intent(TextViewTest.this, SpannableStringActivity.class));
                break;
        }
    }
}
