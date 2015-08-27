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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class CommunicationActivity extends Activity {
    private static final String TAG = "##skywang-CommunicationActivity";
	private static final UUID S_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String mMacAddress;
    private EditText mEtMsg;
    private TextView mTvMac;
    private TextView mTvInfo;
    private BluetoothAdapter mBluetoothAdapter = null;

    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTvInfo.append("\n"+(String)msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_activity);

        // 读取MAC地址
        readBtMac();
        // 初始化蓝牙
        initBt();
        // 连接蓝牙
        connect();

        // 显示消息的TextView
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mTvInfo.setText("Not Connected!");
        // 编辑器
        mEtMsg = (EditText) findViewById(R.id.msg);
        // 发送消息
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEtMsg.getText().toString();
                if (mConnectedThread != null ) {
                    mConnectedThread.write(msg.getBytes());  		
                }
                Log.d(TAG, "send message:"+msg);
            }
        });
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

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
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
                // 通过反射获取Socket
                // tmp =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device, 1);
                tmp = device.createRfcommSocketToServiceRecord(S_UUID);
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
                    break ;
                } catch (IOException e) {
                    Log.e(TAG, "connect failed", e);
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
            Log.d(TAG, "connect finish, retries="+retries+", bSuccess="+bSuccess);
            mHandler.sendMessage(mHandler.obtainMessage(0, bSuccess==true ? "Connect Success!" : "Connect failed!"));

            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * 与BT服务器进行通信的进程
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // 不断监听输入(即服务器发过来的数据)
            while (true) {
                try {
                    // 从InputStream中读取数据
                    bytes = mmInStream.read(buffer);
                    // 将数据显示出来
                    mHandler.sendMessage(mHandler.obtainMessage(5, "RECEIVE: "+new String(buffer)));
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    break;
                }
            }
        }

        /**
         * 发送数据给BT服务器
         */
        public void write(byte[] buffer) {
            try {
                // 将数据写入到OutputStream中
                mmOutStream.write(buffer);
                // 显示发送数据
                mHandler.sendMessage(mHandler.obtainMessage(0, "SEND: "+new String(buffer)));
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
