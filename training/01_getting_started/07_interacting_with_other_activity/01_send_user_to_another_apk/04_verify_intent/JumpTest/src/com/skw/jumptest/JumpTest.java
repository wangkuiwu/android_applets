package com.skw.jumptest;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import org.apache.http.protocol.HTTP;

import java.util.Calendar;
import java.util.List;

public class JumpTest extends Activity 
    implements View.OnClickListener {

    private static final String ACTION_JUMP = "com.skw.jumptest.JumpAction";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button mJump = (Button) findViewById(R.id.bt_jump);
        mJump.setOnClickListener(this);

        Button mMap = (Button) findViewById(R.id.bt_map);
        mMap.setOnClickListener(this);

        Button mWeb = (Button) findViewById(R.id.bt_web);
        mWeb.setOnClickListener(this);

        Button mEmail = (Button) findViewById(R.id.bt_email);
        mEmail.setOnClickListener(this);

        Button mCalendar = (Button) findViewById(R.id.bt_calendar);
        mCalendar.setOnClickListener(this);
    }

    @Override 
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bt_jump:
                jump();
                break;
            case R.id.bt_map:
                viewMap();
                break;
            case R.id.bt_web:
                viewWebPage();
                break;
            case R.id.bt_email:
                viewEmail();
                break;
            case R.id.bt_calendar:
                viewCalendar();
                break;
        }
    }

    private void jump() {
        Intent intent = new Intent(ACTION_JUMP);
        //startActivity(intent);
        verify(intent);
    }

    private void viewMap() {
        // Map point based on address
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent intent = new Intent(Intent.ACTION_VIEW, location);
        //startActivity(intent);
        verify(intent);
    }

    private void viewWebPage() {
        Uri webpage = Uri.parse("http://www.android.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        //startActivity(intent);
        verify(intent);
    }

    private void viewEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        intent.setType(HTTP.PLAIN_TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
        intent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
        // You can also attach multiple items by passing an ArrayList of Uris
        //startActivity(intent);
        verify(intent);
    }

    private void viewCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI);
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2014, 5, 3, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2014, 5, 3, 10, 30);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        intent.putExtra(Events.TITLE, "Ninja class");
        intent.putExtra(Events.EVENT_LOCATION, "Secret dojo");
        verify(intent);
        //startActivity(intent);
    }

    private void verify(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        int actSize = activities.size();
        Toast.makeText(this, "size:"+actSize, Toast.LENGTH_SHORT).show();
    }
}
