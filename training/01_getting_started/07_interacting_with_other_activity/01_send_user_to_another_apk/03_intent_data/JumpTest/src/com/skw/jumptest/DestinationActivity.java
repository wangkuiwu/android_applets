package com.skw.jumptest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DestinationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("this is Destination Activity");

        // Set the text view as the activity layout
        setContentView(textView);
    }
}
