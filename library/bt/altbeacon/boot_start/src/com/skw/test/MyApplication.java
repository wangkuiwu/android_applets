package com.skw.test;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.altbeacon.beacon.startup.BootstrapNotifier;

/**
 * 开机启动时，若"BT是自动打开"并且"检测到Beacon基站"，则自动弹出该程序。
 */
public class MyApplication extends Application implements BootstrapNotifier {
    private static final String TAG = "##skywang-App";
    private RegionBootstrap mRegionBootstrap;
    private BackgroundPowerSaver mBackgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MainActivity mActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        BeaconManager mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen
        mRegionBootstrap = new RegionBootstrap(this, new Region("backgroundRegion", null, null, null));

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        mBackgroundPowerSaver = new BackgroundPowerSaver(this);

        // 监听蓝牙开启/关闭事件
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(new MyReceiver(), filter);
    }

    /**
     * 进入一个Beacon区域的回调函数
     */
    @Override
    public void didEnterRegion(Region arg0) {

        // 第一次进入一个Beacon区域时，启动MainActivity。
        // 否则，如果当前界面是MainActivity时，则给
        Log.d(TAG, "did enter region, haveDetectedBeaconsSinceBoot="+haveDetectedBeaconsSinceBoot+
                ", mActivity="+mActivity);
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");
            if (mActivity == null) {
                // 启动MainActivity。
                // 建议将MainActivity设置为android:launchMode="singleInstance"；确保不会产生多个MainActivity实例。
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
            }
            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (mActivity != null) {
                mActivity.logToDisplay("I see a beacon again" );
            } else {
                Log.d(TAG, "Sending notification.");
                // 给出提示
                sendNotification();
            }
        }
    }

    /**
     * 离开一个Beacon区域的回调函数
     */
    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "did Exit Region");
        if (mActivity != null) {
            mActivity.logToDisplay("I no longer see a beacon.");
        }
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        if (mActivity != null) {
            mActivity.logToDisplay("I have just switched from seeing/not seeing beacons: " + state);
        }
    }

    /**
     * 发送通知
     */
    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("My Application")
                        .setContentText("Anker beacon is nearby.")
                        .setSmallIcon(R.drawable.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void setMonitoringActivity(MainActivity activity) {
        this.mActivity = activity;
    }

    private final class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()) {
                    Log.d(TAG, "bt is enabled! state="+state);
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    Log.d(TAG, "bt is not enabled! state="+state);
                }
            }
        }
    }; 
}
