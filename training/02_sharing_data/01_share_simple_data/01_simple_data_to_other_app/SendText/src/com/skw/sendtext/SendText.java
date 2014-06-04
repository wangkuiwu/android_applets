package com.skw.sendtext;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ComponentName;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class SendText extends Activity 
    implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button mSend = (Button) findViewById(R.id.bt_send);
        mSend.setOnClickListener(this);
    }

    @Override 
    public void onClick(View v) {
        jump();
    }

    private void jump() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
