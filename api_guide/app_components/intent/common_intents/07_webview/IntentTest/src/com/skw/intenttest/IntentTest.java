package com.skw.intenttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.net.Uri;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;

import java.util.Calendar;

public class IntentTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void sendMessage(View view) {
        openWebPage("http://www.baidu.com");
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
