package com.skw.activitytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

public class SecondActivity extends Activity {

    private static final String TAG="##SecondActivity##";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        Log.d(TAG, "onCreate: "+this.toString()+", taskId="+this.getTaskId());
        TextView tv = (TextView) findViewById(R.id.tv2);
        tv.setText(this.toString()+", taskId="+this.getTaskId());
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, ActivityTest.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: intent="+intent+", activity="+this+", taskId="+this.getTaskId());
    }   

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: activity="+this+", taskId="+this.getTaskId());
    }
}
