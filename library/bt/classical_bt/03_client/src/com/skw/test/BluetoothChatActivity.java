package com.skw.test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import com.skw.test.BluetoothService.LocalBinder;
import com.skw.test.util.BtUtil;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothChatActivity extends Activity {
    private static final String TAG = "##skywang-BluetoothChat";
    private static final int REQUEST_SCAN_BT_DEVICES = 1;
    private static final String BT_MAC = "00:00:39:00:84:2A";

    private String mMacAddress = null;
    private EditText mEtMsg;
    private EditText mEtPackageCount;
    private EditText mEtPackageUnit;
    private EditText mEtPackageInterval;
    private TextView mTvBasicInfo;
    private TextView mTvConnectInfo;

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
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBluetoothService.setBluetoothHandler(null);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MSG_BT_MSG_RECEIVE: { // 接收数据
                    StringBuilder sb = new StringBuilder();
                    sb.append("RX: ").append((String)msg.obj).append("\n");
                    mTvConnectInfo.append(sb.toString());
                    return ;
                }
                case BluetoothService.MSG_BT_MSG_WRITE: { // 发送数据
                    StringBuilder sb = new StringBuilder();
                    sb.append("TX: ").append((String)msg.obj).append("\n");
                    mTvConnectInfo.append(sb.toString());
                    return ;
                }
                case BluetoothService.MSG_BT_CONNECT_FINISHED: { // 连接时间
                    StringBuilder sb = new StringBuilder();
                    sb.append("Connection-").append(msg.arg2).append(": time=").append(msg.arg1).append("ms").append(" state=").append((boolean)msg.obj).append("\n");
                    mTvConnectInfo.append(sb.toString());
                    break;
                }
                case BluetoothService.MSG_BT_DISCONNECT_FINISHED: { // 断开连接时间
                    StringBuilder sb = new StringBuilder();
                    sb.append("Disconnection: time=").append(msg.arg1).append("ms").append(" state=").append((boolean)msg.obj).append("\n");
                    mTvConnectInfo.append(sb.toString());
                    break;
                }
                case BluetoothService.MSG_BT_WRITE_START: {
                    mTvConnectInfo.append((String)msg.obj);
                    break;
                }
                case BluetoothService.MSG_BT_WRITE_INFO: {
                    mTvConnectInfo.append((String)msg.obj);
                    break;
                }
                case BluetoothService.MSG_BT_WRITE_FINISHED: {
                    mTvConnectInfo.append("Finished\n");
                    break;
                }
                default:
                    break;
            }
            // 更新蓝牙信息
            if (mBluetoothService!=null && (!TextUtils.isEmpty(mMacAddress))) {
                Log.d(TAG, "read info");
                String info = mBluetoothService.readBluetoothInfo(mMacAddress);
                mTvBasicInfo.setText(info);
            }
            // 更新菜单
            invalidateOptionsMenu();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 禁用“进入该界面之后，自动弹出软键盘”
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.bt_chat_activity);

        // 显示消息的TextView
        mTvConnectInfo = (TextView) findViewById(R.id.tv_info);
        mTvBasicInfo = (TextView) findViewById(R.id.tv_basic_info);
        // 编辑器
        mEtMsg = (EditText) findViewById(R.id.msg);
        mEtPackageUnit = (EditText) findViewById(R.id.et_package_unit);
        mEtPackageCount = (EditText) findViewById(R.id.et_package_count);
        mEtPackageInterval = (EditText) findViewById(R.id.et_package_interval);
        // 发送消息
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEtMsg.getText().toString();
                Log.d(TAG, "send message:"+text);
                if (mBluetoothService != null) {
                    mBluetoothService.sendBluetoothMessage(text);
                    mEtMsg.setText("");
                }
            }
        });

        // 启动服务，用来更新进度
        mServiceIntent = new Intent(BluetoothChatActivity.this, BluetoothService.class);        
        bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bt_chat_menu, menu);
        // 如果在发送数据，则显示"发送"；否则，显示停止
        if (mBluetoothService!=null && mBluetoothService.isDataTransfer()) {
            menu.findItem(R.id.start_send).setVisible(false);
            menu.findItem(R.id.stop_send).setVisible(true);
        } else {
            menu.findItem(R.id.start_send).setVisible(true);
            menu.findItem(R.id.stop_send).setVisible(false);
        }

        // 如果蓝牙已经绑定，则显示"unbond"；否则，显示"bond"
        if (mBluetoothService!=null && (!TextUtils.isEmpty(mMacAddress)) && mBluetoothService.isBluetoothBonded(mMacAddress)) {
            menu.findItem(R.id.bt_bond).setVisible(false);
            menu.findItem(R.id.bt_unbond).setVisible(true);
        } else {
            menu.findItem(R.id.bt_bond).setVisible(true);
            menu.findItem(R.id.bt_unbond).setVisible(false);
        }

        // 如果蓝牙已经连接，则显示"断开"按钮；否则，显示"连接"按钮
        if (mBluetoothService!=null && mBluetoothService.isBluetoothConnected()) {
            menu.findItem(R.id.bt_connect).setVisible(false);
            menu.findItem(R.id.bt_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.bt_connect).setVisible(true);
            menu.findItem(R.id.bt_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.start_send:
                int unit = getNumber(mEtPackageUnit);
                int loop = getNumber(mEtPackageCount);
                int interval = getNumber(mEtPackageInterval);
                mBluetoothService.startContiniusWrite(unit, loop, interval);
                return true;
            case R.id.stop_send:
                mBluetoothService.stopContiniusWrite();
                return true;
            case R.id.bt_scan: {
                // 获取蓝牙地址之前，清除之前的蓝牙地址
                mMacAddress = null;
                Intent serverIntent = new Intent(this, BluetoothScanActivity.class);
                startActivityForResult(serverIntent, REQUEST_SCAN_BT_DEVICES);
                return true;
            }
            case R.id.bt_bond:
                if (!TextUtils.isEmpty(mMacAddress)) {
                    mBluetoothService.bondBluetooth(mMacAddress);
                }
                return true;
            case R.id.bt_unbond:
                if (!TextUtils.isEmpty(mMacAddress)) {
                    mBluetoothService.unbondBluetooth(mMacAddress);
                }
                return true;
            case R.id.bt_connect:
                if (!TextUtils.isEmpty(mMacAddress)) {
                    mBluetoothService.connectBluetooth(mMacAddress);
                }
                return true;
            case R.id.bt_disconnect:
                if (mBluetoothService.isBluetoothConnected()) {
                    mBluetoothService.disconnectBluetooth();
                }
                return true;
            case R.id.bt_auto:
                mBluetoothService.runAutoThread(BT_MAC);
                return true;
            case R.id.clear_msg:
                mTvConnectInfo.setText("");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getNumber(TextView tv) {
        int ret = 0;
        try {
            String text = tv.getText().toString();
            ret = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当该Activity可见时，监听蓝牙服务的消息
        if (mBluetoothService != null) {
            mBluetoothService.setBluetoothHandler(mHandler);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
        switch (requestCode) {
            case REQUEST_SCAN_BT_DEVICES:
                if (resultCode == Activity.RESULT_OK) {
                    mMacAddress = data.getExtras().getString(BluetoothScanActivity.EXTRA_DEVICE_ADDRESS);
                    mTvBasicInfo.setText(mBluetoothService.readBluetoothInfo(mMacAddress));
                    invalidateOptionsMenu();
                }
                break;
        }
    }
}
