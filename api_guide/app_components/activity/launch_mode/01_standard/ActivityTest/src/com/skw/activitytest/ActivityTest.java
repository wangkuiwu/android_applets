package com.skw.activitytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

public class ActivityTest extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(this.toString()+", taskId="+this.getTaskId());
    }

    public void onJump(View view) {
        Intent intent = new Intent(this, ActivityTest.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: intent="+intent);
    }   
}
