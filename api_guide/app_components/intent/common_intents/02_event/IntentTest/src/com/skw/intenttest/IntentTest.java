package com.skw.intenttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
        switch (view.getId()) {
            case R.id.btn_event:
                Calendar begin = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.DATE, 1);
                addEvent("MyEvent", null, begin, end) ;
                break;
        }
    }

    /**
     * 记事
     */
    public void addEvent(String title, String location, Calendar begin, Calendar end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(Events.TITLE, title)
                .putExtra(Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
