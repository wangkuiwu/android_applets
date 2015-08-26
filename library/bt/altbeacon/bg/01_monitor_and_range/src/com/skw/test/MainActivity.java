package com.skw.test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * AltBeacon测试程序
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onResume() {
        checkBluetooth();
        super.onResume();
    }

    /**
     * 检测蓝牙是否打开
     */
    private void checkBluetooth() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }
    }   
}
