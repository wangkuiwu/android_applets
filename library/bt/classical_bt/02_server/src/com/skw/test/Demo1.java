package com.skw.test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import java.util.Set;
/**
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class Demo1 extends Activity {
    private static final String TAG = "##skywang-Demo1";
    private static final int REQUEST_ENABLE_BT = 1;

    private TextView mTvInfo;

    private BluetoothAdapter mBluetoothAdapter = null;

    // 扫描列表
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ListView mNewListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);

        // 初始化蓝牙
        initBt();

        // 显示消息的TextView
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        // 扫描开关
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BT扫描
                doDiscovery();
            }
        });

        // 监听蓝牙扫描广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        // 扫描列表
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        mNewListView = (ListView) findViewById(R.id.list_new_devices);
        mNewListView.setAdapter(mNewDevicesArrayAdapter);
        mNewListView.setOnItemClickListener(mDeviceClickListener);

        // 读取绑定的蓝牙设备
        readBoundedDevices();
    }

    /**
     * 初始化蓝牙
     */
    private void initBt() {
        // 获取默认的BluetoothAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return ;
        }
    }

    /**
     * 检查BT是否打开，没有打开的话，则弹出打开BT确认对话框
     */
    private void checkBluetooth() {
        // 如果BT没有打开，则弹出"打开BT的提示窗口"
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * 读取已绑定的蓝牙列表
     */
    private void readBoundedDevices() {
        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        final ArrayAdapter boundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        ListView boundListView = (ListView) findViewById(R.id.list_bound_devices);
        boundListView.setAdapter(boundAdapter);
        boundListView.setOnItemClickListener(mDeviceClickListener);

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                boundAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            boundAdapter.add("No Bound Device");
        }
    }

    /**
     * BT扫描
     */
    private void doDiscovery() {
        Log.d(TAG, "doDiscovery");

        mTvInfo.setText("scanning...");

        // 停止扫描
        cancelDiscovery();

        // 开始扫描
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * 停止扫描
     */
    private void cancelDiscovery() {
        // 若已经在扫描中，则先停止扫描
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkBluetooth();
    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();
        cancelDiscovery();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth is not opened", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    /**
     * 点击扫描/绑定的蓝牙设备的回调
     */
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            cancelDiscovery();

            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent(Demo1.this, Demo2.class);
            intent.putExtra("bt_address", address);
            startActivity(intent);
        }
    };


    /**
     * BT扫描广播
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 获取扫描到的BT设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 如果该BT配备已经配对过，则忽略(因为已经添加到mDeviceList中)；否则，添加到mDeviceList中
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mTvInfo.setText("scan finish! please select a device.");
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    mNewDevicesArrayAdapter.add("No Device Found!");
                }
            }
        }
    };
}
