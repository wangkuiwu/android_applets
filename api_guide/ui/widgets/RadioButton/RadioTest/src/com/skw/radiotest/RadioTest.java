package com.skw.radiotest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RadioTest extends Activity {

    private TextView mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mView = (TextView) findViewById(R.id.tv_intro);

        RadioButton mTwo = (RadioButton) findViewById(R.id.radio_two);
        mTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mView.setText("two checked!");
                } else {
                    mView.setText("two un-checked!");
                }
            }
        });
 
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
     
        switch(view.getId()) {
            case R.id.radio_one:
                Toast.makeText(this, "one checked="+checked, Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_two:
                Toast.makeText(this, "two checked="+checked, Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_three:
                Toast.makeText(this, "three checked="+checked, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
