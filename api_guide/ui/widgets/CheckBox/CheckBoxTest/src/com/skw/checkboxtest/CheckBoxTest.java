package com.skw.checkboxtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CheckBoxTest extends Activity {

    private TextView mView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mView = (TextView) findViewById(R.id.tv_view);

        CheckBox mSend = (CheckBox) findViewById(R.id.cb_send);
        mSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mView.setText("selected!");
                } else {
                    mView.setText("un-selected!");
                }
            }
        });
    }

    public void onSimpleClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
 
        switch(view.getId()) {
            case R.id.cb_simple:
                Toast.makeText(this, "simple checked="+checked, Toast.LENGTH_SHORT).show();
                break;
            case R.id.cb_send:
                Toast.makeText(this, "send checked="+checked, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
