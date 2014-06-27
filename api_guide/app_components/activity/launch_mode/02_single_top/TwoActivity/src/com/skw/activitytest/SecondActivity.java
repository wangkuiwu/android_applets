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

        Log.d(TAG, "onCreate: "+this.toString());
        TextView tv = (TextView) findViewById(R.id.tv2);
        tv.setText(this.toString());
    }

    public void onBack(View view) {
        Log.d(TAG, "onBack: "+this.toString());
        Intent intent = new Intent(this, ActivityTest.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: intent="+intent+", activity="+this);
    }   
}
