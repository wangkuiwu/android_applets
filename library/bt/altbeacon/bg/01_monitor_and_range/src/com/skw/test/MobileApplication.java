package com.skw.test;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MobileApplication extends Application {
    private static final String TAG = "##skywang-Application";

    @Override
    public void onCreate() {
        super.onCreate();

        // 启动MobileBeaconService
        startService(new Intent(this, MobileBeaconService.class));
    }
}
