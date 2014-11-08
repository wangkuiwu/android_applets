package com.skw.viewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ViewTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button mTip = (Button)findViewById(R.id.tip);
        mTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTip();
            }
        });
    }

    private void showTip() {
        Toast.makeText(this, "hi, sky", 0).show();
    }
}
