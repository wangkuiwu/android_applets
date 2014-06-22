package com.skw.dialogtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class DialogTest extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        showDialog();
    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DialogA dialoga = new DialogA();
        dialoga.show(fm, "fragmenta");
    }
}
