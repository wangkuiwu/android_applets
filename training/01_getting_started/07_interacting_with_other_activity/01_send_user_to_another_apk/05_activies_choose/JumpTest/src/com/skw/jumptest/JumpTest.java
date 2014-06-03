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

        Button mEmail2 = (Button) findViewById(R.id.bt_email2);
        mEmail2.setOnClickListener(this);

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
            case R.id.bt_email2:
                viewEmail2();
                break;
            case R.id.bt_calendar:
                viewCalendar();
                break;
        }
    }

    private void jump() {
        Intent intent = new Intent(ACTION_JUMP);
        //startActivity(intent);
        choose(intent);
    }

    private void viewMap() {
        // Map point based on address
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent intent = new Intent(Intent.ACTION_VIEW, location);
        //startActivity(intent);
        choose(intent);
    }

    private void viewWebPage() {
        Uri webpage = Uri.parse("http://www.android.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        //startActivity(intent);
        choose(intent);
    }

    // 自定义"选择对话框"的标题
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
        choose(intent);
    }

    // 采用系统默认的"选择对话框"的标题
    private void viewEmail2() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        intent.setType(HTTP.PLAIN_TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
        intent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
        // You can also attach multiple items by passing an ArrayList of Uris
        startActivity(intent);
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
        choose(intent);
        //startActivity(intent);
    }

    private void choose(Intent intent) {
        String title = getResources().getString(R.string.app_name);
        // Create intent to show chooser
        Intent chooser = Intent.createChooser(intent, title);

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
