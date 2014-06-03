package com.skw.startactivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class Activity01 extends Activity 
    implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity01);

        Button mReturn = (Button) findViewById(R.id.bt_rt1);
        mReturn.setOnClickListener(this);
    }

    @Override 
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(StartActivity.NAME, "ONE");

        this.setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
