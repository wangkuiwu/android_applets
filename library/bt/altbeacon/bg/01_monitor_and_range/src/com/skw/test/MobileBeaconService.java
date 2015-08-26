package com.skw.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Collection;
import java.util.Iterator;
      
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

/**
 * Beacon后台服务
 *
 * @author sky
 * @e-mail sky.wang@oceanwing.com
 * @date 2015-08-13
 */
public class MobileBeaconService extends Service implements BeaconConsumer {
    private static final String TAG = "##skywang-MobileBeaconService";

    private BeaconManager mBeaconManager;
    private Region mRangeRegion = new Region("service_RangeRegion", null, null, null);
    private Region mMonitorRegion = new Region("service_MonitorRegion", null, null, null);

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        initBeacon();
    }

    private void initBeacon() {
        // 获取BeaconManager对象
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // 设置监听的Beacon的类型。这里的"m:2-3=0215"是苹果设备(iPhone, iPad作为Beacon基站)的前缀。
        // (1) m:2-3=0215   第2-3个字节，表示Beacon基站的prefix。苹果设备作为基站时的prefix固定是0215
        // (2) i:4-19       第4-19个字节，表示UUID(即独立的设备ID)。
        // (3) i:20-21      第20-21个字节，表示major
        // (4) i:22-23      第22-23个字节，表示minor
        // (5) p:24-24      第24个字节，表示RSSI。信号强度or距离
        // (6) d:25-25      第25个字节，data field。
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        // 设置"进入/退出区域"的监视器
        mBeaconManager.setMonitorNotifier(new MyMonitorNotifier());
        // 设置"周期发送iBeacon广播之后"的反馈监视器
        mBeaconManager.setRangeNotifier(new MyRangeNotifier());
        // 绑定到BeaconService服务，绑定成功后会回调onBeaconServiceConnect()
        mBeaconManager.bind(this);
    }

    /**
     * 当前Service连上BeaconService服务时的回调函数
     */
    @Override
    public void onBeaconServiceConnect() {
        try {
            mBeaconManager.startMonitoringBeaconsInRegion(mMonitorRegion);
            mBeaconManager.startRangingBeaconsInRegion(mRangeRegion);
        } catch (RemoteException e) {   }   
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        // 与BeaconService服务解除绑定
        mBeaconManager.unbind(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    // 通知id。通知id不一样，则每次都会生成一则通知；否则会覆盖原有的通知。
    private static int mNotifyIndex = 0;
    /**
     * 发送一个通知
     */
    private static void generateNotification(Context context, String message) {
        Intent launchIntent = new Intent(context, MainActivity.class).
            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).
            notify(mNotifyIndex++, new NotificationCompat.Builder(context)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setTicker(message)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(message)
                    .setContentIntent(PendingIntent.getActivity(context, 1, launchIntent, 0))
                    .setAutoCancel(true)
                    // 设置：加入震動效果(DEFAULT_VIBRATE), 加入音效效果(DEFAULT_SOUND), 加入閃燈效果(DEFAULT_LIGHTS)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .build());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, (String)msg.obj);
            generateNotification(MobileBeaconService.this, (String)msg.obj);
        }
    };

    /**
     * iBeacon扫描信号监听器(用于监听iBeacon的周期扫描信息，周期的频率一般是1s)
     */
    private boolean mMacNotified = false;
    private class MyRangeNotifier implements RangeNotifier {
        @Override
        public void didRangeBeaconsInRegion(final Collection<Beacon> rangedBeacons, Region region) {
            Log.i(TAG, "range: beacons="+rangedBeacons.size()+", region="+region.getUniqueId());
            if (rangedBeacons.size()>0) {
                Beacon bb = null;
                Iterator<Beacon> iter = rangedBeacons.iterator();
                while (iter.hasNext()) {
                    bb = iter.next();
                    Log.d(TAG, "RangeBeacon: beacon="+bb);
                }

                // 选出检测到的iBeacon中的最后一个，并发送通知
                if ((!mMacNotified) && (bb!=null)) {
                    mMacNotified = true;
                    mHandler.sendMessage(mHandler.obtainMessage(4, "iBeacon Last: mac:"+bb.getBluetoothAddress()+", uuid:"+bb.getId1()));
                }
            }
        }
    }

    /**
     * iBeacon监视器(用于监听进出iBeacon基站区域)
     *
     * @desc 
     *   (1) 当进入iBeacon区域时会回调didEnterRegion()
     *   (2) 当退出iBeacon区域时会回调didExitRegion()
     */
    private class MyMonitorNotifier implements MonitorNotifier {
        @Override
        public void didEnterRegion(Region region) {
            Log.i(TAG, "I see an beacon");
            mHandler.sendMessage(mHandler.obtainMessage(0, new String("iBeacon Enter region="+region)));
        }

        @Override
        public void didExitRegion(Region region) {
            Log.i(TAG, "I no longer see an beacon");
            mHandler.sendMessage(mHandler.obtainMessage(1, new String("iBeacon Exit region="+region)));
        }

        @Override
        public void didDetermineStateForRegion(int state, Region region) {
            Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);        
        }
    }
}
