package com.skw.toggletest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ToggleTest extends Activity {

    private TextView mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mView = (TextView) findViewById(R.id.tv_intro);

        ToggleButton mSync = (ToggleButton) findViewById(R.id.tb_sync);
        mSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mView.setText("checked!");
                } else {
                    mView.setText("un-checked!");
                }
            }
        });
    }

    public void onToggleClicked(View view) {
        boolean checked = ((ToggleButton) view).isChecked();
     
        switch(view.getId()) {
            case R.id.tb_simple:
                Toast.makeText(this, "simple checked="+checked, Toast.LENGTH_SHORT).show();
                break;
            case R.id.tb_sync:
                Toast.makeText(this, "sync checked="+checked, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
