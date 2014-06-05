package com.skw.jumptest;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class DestinationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent that started this activity
        Intent intent = getIntent();

        // Figure out what to do based on the intent type
        if (intent.getType().indexOf("image/") != -1) {
            // TODO
            setTextView("Image IS No Support Yet!");
        } else if (intent.getType().equals("text/plain")) {
            setTextView(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    private void setTextView(String text) {
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(text);

        // Set the text view as the activity layout
        setContentView(textView);
    }
}
