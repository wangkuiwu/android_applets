package com.skw.test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothServerSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * BT(2.0) 的配对和连接基本用法
 *
 * @desc
 * (1) 进入BoundActivity之后，会自动进行蓝牙配对。
 * (2) 配对成功后，会自动进行连接
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class BoundActivity extends Activity {
    private static final String TAG = "##skywang-BoundActivity";
    private static final int REQUEST_ENABLE_BT = 1;

    private String mMacAddress;
    private TextView mTvMac;
    private TextView mTvInfo;
    private BluetoothAdapter mBluetoothAdapter = null;

    private ConnectThread mConnectThread;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTvInfo.append("\n"+(String)msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bound_activity);

        // 读取MAC地址
        readBtMac();
        // 初始化蓝牙
        initBt();
        // 连接蓝牙
        connect();

        // 显示消息的TextView
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mTvInfo.setText("Not Connected!");
    }

    private void readBtMac() {
        Intent intent = getIntent();
        mMacAddress = intent.getStringExtra("bt_address");
        TextView tvMac = (TextView) findViewById(R.id.tv_mac);
        tvMac.setText("mac: "+mMacAddress);
    }

    private void initBt() {
        // 获取默认的BluetoothAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return ;
        }
    }

    private void connect() {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mMacAddress);
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    private void stopConnect() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
    }


    @Override 
    protected void onDestroy() {
        super.onDestroy();
        stopConnect();
    }

    private final class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device) {
            boolean bSuccess = false;;
            mmDevice = device;
            BluetoothSocket tmp = null;

            Method method;
            try {
                // 通过反射获取Socket。
                // 注意，这里通过device.createRfcommSocketToServiceRecord("00001101-0000-1000-8000-00805F9B34FB")一般都会失败。
                // tmp = device.createRfcommSocketToServiceRecord("00001101-0000-1000-8000-00805F9B34FB");
                tmp =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device, 1);
                bSuccess = true;
            } catch (Exception e) {
                Log.e(TAG, "create socket failed", e);
            }
            mHandler.sendMessage(mHandler.obtainMessage(0, bSuccess ? "create socket success" : "create socket Failed!"));

            mmSocket = tmp;
        }

        @Override
        public void run() {
            // 1. 连接建立之前的先配对
            boolean bBounded = false;
            try {
                if (mmDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Method method = mmDevice.getClass().getMethod("createBond");    
                    Log.e("TAG", "start bounding");    
                    method.invoke(mmDevice);
                    bBounded = true;
                } else {
                    bBounded = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "bound failed", e);
            }
            mHandler.sendMessage(mHandler.obtainMessage(0, bBounded ? "bound success" : "bound Failed!"));


            // 2. 建立连接
            boolean bSuccess = false;
            int retries = 0;
            // 取消扫描
            mBluetoothAdapter.cancelDiscovery();
            mHandler.sendMessage(mHandler.obtainMessage(0, new String("Connecting...")));
            // 尝试连接3次
            while (retries++ < 3) {
                try {
                    mmSocket.connect();
                    bSuccess = true;
                    // 连接成功，则跳出while循环。
                    break ;
                } catch (IOException e) {
                    Log.e(TAG, "connect failed retries: "+retries, e);
                    try {
                        mmSocket.close();
                    } catch (IOException e2) {
                        Log.e(TAG, "unable to close() socket during connection failure", e2);
                    }
                }

                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    Log.e(TAG, "connect interruption ", e);
                }
            }
            Log.d(TAG, "connect finish. bSuccess="+bSuccess);
            mHandler.sendMessage(mHandler.obtainMessage(0, bSuccess==true ? "Connect Success!" : "Connect failed!"));
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close of connect socket failed", e);
            }
        }
    }
}
