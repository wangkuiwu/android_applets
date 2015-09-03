package com.skw.test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import com.skw.test.BluetoothService.LocalBinder;
import java.util.Set;

public class BluetoothScanActivity extends Activity {
    private static final String TAG = "##skywang-BluetoothScanActivity";

    private static final int REQUEST_ENABLE_BT = 0x1;
    public static final String EXTRA_DEVICE_ADDRESS = "device_address";

    private TextView mTvPairedDevices;
    // 已配对列表
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ListView mPairedListView;
    // 扫描列表
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ListView mNewListView;

    private Intent mServiceIntent;
    private BluetoothService mBluetoothService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            Log.d(TAG, "onServiceConnected");
            // 获取IBinder服务对象
            LocalBinder binder = (LocalBinder) service;
            mBluetoothService = binder.getService();
            mBluetoothService.setBluetoothHandler(mHandler);
            checkBluetooth();
            readBondedDevices();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBluetoothService.setBluetoothHandler(null);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case BluetoothService.MSG_BT_DEVICE_FOUNT: {
                    BluetoothDevice device = (BluetoothDevice)msg.obj;
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    break;
                }
                case BluetoothService.MSG_BT_DISCOVERY_FINISHED: {
                    setProgressBarIndeterminateVisibility(false);
                    invalidateOptionsMenu();
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.bt_scan_activity);

        // 显示消息的TextView
        mTvPairedDevices = (TextView) findViewById(R.id.tv_paired_devices);

        // 扫描列表
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        mNewListView = (ListView) findViewById(R.id.list_new_devices);
        mNewListView.setAdapter(mNewDevicesArrayAdapter);
        mNewListView.setOnItemClickListener(mDeviceClickListener);
        // 已配对列表
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        mPairedListView = (ListView) findViewById(R.id.list_paired_devices);
        mPairedListView.setAdapter(mPairedDevicesArrayAdapter);
        mPairedListView.setOnItemClickListener(mDeviceClickListener);

        // 启动服务，用来更新进度
        mServiceIntent = new Intent(BluetoothScanActivity.this, BluetoothService.class);        
        bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 读取已配对的蓝牙设备
     */
    private void readBondedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothService.readBondedDevices();

        if (pairedDevices.size() > 0) {
            mTvPairedDevices.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            mPairedDevicesArrayAdapter.add("No Paired Devices.");
        }
    }

    private void checkBluetooth() {
        if (mBluetoothService!=null && (!mBluetoothService.isBluetoothEnabled())) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bt_scan_menu, menu);
        if (mBluetoothService!=null && mBluetoothService.isDiscovering()) {
            menu.findItem(R.id.start_scan).setVisible(false);
            menu.findItem(R.id.stop_scan).setVisible(true);
        } else {
            menu.findItem(R.id.start_scan).setVisible(true);
            menu.findItem(R.id.stop_scan).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.start_scan:
                setProgressBarIndeterminateVisibility(true);
                mNewDevicesArrayAdapter.clear();
                mBluetoothService.doBluetoothDiscovery(true);
                invalidateOptionsMenu();
                return true;
            case R.id.stop_scan:
                mBluetoothService.doBluetoothDiscovery(false);
                setProgressBarIndeterminateVisibility(false);
                invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();

        if(mServiceIntent != null) {
            unbindService(mConnection);
            mServiceIntent = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth is not opened", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    /**
     * 点击扫描/绑定的蓝牙设备的回调
     */
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mBluetoothService.doBluetoothDiscovery(false);

            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Log.d(TAG, "click item address:"+address);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
}
