package com.skw.dialogtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class DialogTest extends FragmentActivity 
    implements DialogA.NoticeDialogListener {

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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Toast.makeText(this, "Positive Callback", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(this, "Negative Callback", Toast.LENGTH_SHORT).show();
    }
}
