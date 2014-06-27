package com.skw.single;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

public class Single extends Activity {
    private static final String TAG="##Single##";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView tv = (TextView)findViewById(R.id.info);
        tv.setText(this.toString()+", taskId="+this.getTaskId());
        Log.d(TAG, this.toString()+", taskId="+this.getTaskId());
    }
}
