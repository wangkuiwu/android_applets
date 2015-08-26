package com.skw.test;

import android.app.Activity;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * AltBeacon测试程序
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class MainActivity extends Activity {
    private final String TAG = "##skywang-Main";

    private TextView mTvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTvInfo = (TextView) findViewById(R.id.tv_info);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(MobileBeaconService.ACTION_BACKGROUND_BY_BEACON);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (MobileBeaconService.ACTION_BACKGROUND_BY_BEACON.equals(action)) {
                String msg = intent.getStringExtra("message");
                Log.d(TAG, "msg="+msg);
                mTvInfo.append("\n\n"+msg);
            }
        }
    };
}
