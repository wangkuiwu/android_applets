package com.skw.activitytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

public class ActivityTest extends Activity {
    private static final String TAG="##ActivityTest##";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d(TAG, "onCreate");

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(this.toString());
    }

    public void onJump(View view) {
        Log.d(TAG, "onJump");
        Intent intent = new Intent(this, ActivityTest.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: intent="+intent);
    }
}
