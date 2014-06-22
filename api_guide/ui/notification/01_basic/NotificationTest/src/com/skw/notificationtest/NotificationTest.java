package com.skw.notificationtest;

import android.app.Activity;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class NotificationTest extends Activity {

    private static final int HELLO_ID = 0x01;
    private NotificationManager mNotificationManager;  
    private Notification mNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 获取NotificationManager的引用
        String ns = Context.NOTIFICATION_SERVICE;  
        mNotificationManager = (NotificationManager)this.getSystemService(ns);  


        // 初始化Notification  
        int icon = R.drawable.ic_action_call;
        CharSequence text = "S_Note";
        long when = System.currentTimeMillis();  
        mNotification = new Notification(icon, text, when);  
        // 铃声和震动方式等都是默认
        mNotification.defaults = Notification.DEFAULT_ALL;  
        // 点击之后，自动消失
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        /*
        mNotification.flags |= Notification.FLAG_NO_CLEAR;  
        mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;  
        // */
        // 定义notification的消息 和 PendingIntent  
        Intent notificationIntent = new Intent(NotificationTest.this, NotificationResult.class);
        PendingIntent contentIntent = PendingIntent.getActivity(NotificationTest.this, 0, notificationIntent, 0);
        mNotification.setLatestEventInfo(NotificationTest.this, "My Title", "My Body", contentIntent);  
    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.send:
                mNotificationManager.notify(HELLO_ID, mNotification);
                break;
            case R.id.cancel:
                mNotificationManager.cancel(HELLO_ID);
                break;
            default:
                break;
        }
    }
}
