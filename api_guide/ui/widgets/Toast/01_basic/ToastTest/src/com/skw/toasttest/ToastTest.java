package com.skw.toasttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ToastTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void sendMessage(View view) {
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
    }
}
