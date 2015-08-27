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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import java.util.Set;
/**
 * BT(2.0) 的基本使用方法示例
 *
 * @desc
 * (1) 打开MainActivity之后，自动进行BT开关侦测。
 *     若BT没打开，则提示打开BT。
 * (2) 自动获取BT以配对的蓝牙设备列表。
 * (3) 点击scan进行扫描。
 * (4) 点击扫描结果，会跳转到BoundActivity进行配对和连接。
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class MainActivity extends Activity {
    private static final String TAG = "##skywang-MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;

    private TextView mTvInfo;

    private BluetoothAdapter mBluetoothAdapter = null;

    // 扫描列表
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ListView mNewListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 显示消息的TextView
        mTvInfo = (TextView) findViewById(R.id.tv_info);

        // 初始化蓝牙
        initBt();

        // 读取绑定的蓝牙设备
        readBoundedDevices();

        // 监听蓝牙扫描广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        // 扫描列表
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        mNewListView = (ListView) findViewById(R.id.list_new_devices);
        mNewListView.setAdapter(mNewDevicesArrayAdapter);
        mNewListView.setOnItemClickListener(mDeviceClickListener);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.scan:
                // 进行BT扫描
                doDiscovery();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

            // 跳转到BoundActivity进行配对&连接。会将蓝牙地址传递到BoundActivity中
            Intent intent = new Intent(MainActivity.this, BoundActivity.class);
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
