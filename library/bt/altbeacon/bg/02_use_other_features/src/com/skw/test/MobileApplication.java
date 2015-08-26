package com.skw.test;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MobileApplication extends Application {
    private static final String TAG = "##skywang-Application";

    private static MobileApplication mApplication;

    public static MobileApplication get() {
        return mApplication;
    }   

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
        startService(new Intent(this, MobileBeaconService.class));
    }
}
