package com.skw.radiotest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class RadioTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
