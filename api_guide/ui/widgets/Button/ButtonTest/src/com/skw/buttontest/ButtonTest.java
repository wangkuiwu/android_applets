package com.skw.buttontest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

public class ButtonTest extends Activity 
    implements View.OnClickListener {

    private Button mBtnPost;
    private Button mBtnTransmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mBtnTransmit = (Button)findViewById(R.id.bt_transmit);
        mBtnTransmit.setOnClickListener(this);

        mBtnPost = (Button)findViewById(R.id.bt_post);
        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "post message!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_transmit:
                Toast.makeText(this, "transmit message!", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    public void sendMessage(View v) {
        Toast.makeText(this, "send message!", Toast.LENGTH_SHORT).show();
    }
}
