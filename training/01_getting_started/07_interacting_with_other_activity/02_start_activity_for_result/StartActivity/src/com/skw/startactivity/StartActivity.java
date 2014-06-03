package com.skw.startactivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ComponentName;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class StartActivity extends Activity
    implements View.OnClickListener {

    private static final String ACTION_01 = "com.skw.startactivity.ACTIVITY1";
    private static final String ACTION_02 = "com.skw.startactivity.ACTIVITY2";
    static final String NAME = "name";

    private TextView mText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button mStart1 = (Button) findViewById(R.id.bt_start1);
        mStart1.setOnClickListener(this);

        Button mStart2 = (Button) findViewById(R.id.bt_start2);
        mStart2.setOnClickListener(this);

        mText = (TextView) findViewById(R.id.bt_text);
    }

    @Override 
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bt_start1:
                start1();
                break;
            case R.id.bt_start2:
                start2();
                break;
            default:
        }
    }

    private void start1() {
        Intent intent = new Intent(ACTION_01);
        // 请求码(requestCode)是1
        startActivityForResult(intent, 1);
    }

    private void start2() {
        Intent intent = new Intent(ACTION_02);
        // 请求码(requestCode)是2
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String name = intent.getStringExtra(NAME);
        switch(requestCode) {
            case 1:
                mText.setText("Activity1 : name="+name+", resultCode="+resultCode);
                break;
            case 2:
                mText.setText("Activity2 : name="+name+", resultCode="+resultCode);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
