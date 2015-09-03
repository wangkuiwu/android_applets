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
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST"; 

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

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); 
        filter.addAction(ACTION_PAIRING_REQUEST); 
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED); 
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        // 读取MAC地址
        readBtMac();
        // 初始化蓝牙
        initBt();
        // 连接蓝牙
        connect();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.communication_menu, menu);
        if (mWriteThread != null) {
            menu.findItem(R.id.start_send).setVisible(false);
            menu.findItem(R.id.stop_send).setVisible(true);
        } else {
            menu.findItem(R.id.start_send).setVisible(true);
            menu.findItem(R.id.stop_send).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.start_send:
                startContiniusWrite();
                invalidateOptionsMenu();
                return true;
            case R.id.stop_send:
                stopContiniusWrite();
                invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();
        stopContiniusWrite();
        stopConnect();
        unregisterReceiver(mReceiver);
    }

    public boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        Method setPinMethod = btClass.getDeclaredMethod("setPin", new Class[]{ byte[].class});
        // byte[] pin = (byte[]) BluetoothDevice.class.getMethod("convertPinToBytes", String.class).invoke(BluetoothDevice.class, str);
        Boolean returnValue = (Boolean) setPinMethod.invoke(btDevice, new Object[]{str.getBytes()});
        Log.e(TAG, "setPin: returnValue=" + returnValue);
        return returnValue.booleanValue();
    }

    public boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    public boolean setPairingConfirmation(Class btClass, BluetoothDevice device) throws Exception {
        Boolean returnValue = (Boolean) device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
        return returnValue.booleanValue();
    }

    public boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
        // Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        // Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        Boolean returnValue = (Boolean) device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
        return returnValue.booleanValue();
    }

    private BluetoothSocket mConnectedSocket = null;
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
                // tmp = device.createInsecureRfcommSocketToServiceRecord(S_UUID);
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
                    // setPin(mmDevice.getClass(), mmDevice, "123456");
                    // mHandler.sendMessage(mHandler.obtainMessage(0, "setpin 123456"));
                    mHandler.sendMessage(mHandler.obtainMessage(0, "create bond"));
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
            mConnectedSocket = mmSocket;

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

        @Override
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


    private WriteThread mWriteThread;
    private void startContiniusWrite() {
        if (mConnectedSocket!=null && mWriteThread==null) {
            mWriteThread = new WriteThread(mConnectedSocket);
            mWriteThread.start();
        }
    }

    private void stopContiniusWrite() {
        if (mWriteThread != null) {
            mWriteThread.interrupt();
            mWriteThread = null;
        }
    }

    private class WriteThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;
        private long mIndex = 0;
        private long mCount = 0;

        public WriteThread(BluetoothSocket socket) {
            mmSocket = socket;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmOutStream = tmpOut;
        }

        private byte[] getData() {
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<2048; i++) {
                sb.append("1");
            }
            return sb.toString().getBytes();
        }

        @Override
        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            // String text = "1234567890";
            // byte[] buffer = text.getBytes();
            byte[] buffer = getData();
            long startTime = System.currentTimeMillis();
            long prevTime = startTime;

            // 不断监听输入(即服务器发过来的数据)
            while (true) {
                try {
                    mIndex++;
                    // 从InputStream中读取数据
                    mmOutStream.write(buffer);
                    sleep(500);
                    // 将数据显示出来
                    long currTime = System.currentTimeMillis();
                    StringBuilder sb = new StringBuilder();
                    mCount += buffer.length;
                    sb.append("write-").append(mIndex)
                        .append(": bytes=").append(mCount)
                        .append(" tTime=").append((currTime-startTime)/1000).append("s");
                        // .append(" sTime=").append(currTime-prevTime).append("ms");
                    // mHandler.sendMessage(mHandler.obtainMessage(5, "write-"+mIndex+": bytes-"+buffer.length+", time:"+(currTime-startTime)/1000));
                    mHandler.sendMessage(mHandler.obtainMessage(5, sb.toString()));
                    prevTime = currTime;

                    if (mIndex == 1000) {
                        mHandler.sendMessage(mHandler.obtainMessage(5, "finished normal!"));
                        break;
                    }
                } catch (InterruptedException e) {
                    mHandler.sendMessage(mHandler.obtainMessage(5, "Write finished normal!"));
                    break;
                } catch (IOException e) {
                    mHandler.sendMessage(mHandler.obtainMessage(5, "Write finished abnormal! IOException"));
                    Log.e(TAG, "disconnected", e);
                    break;
                }
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ACTION_PAIRING_REQUEST)) {
                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mHandler.sendMessage(mHandler.obtainMessage(0, "receiver Pairing Request device:"+btDevice.getAddress()));
                try {
                    boolean bPin = setPin(btDevice.getClass(), btDevice, "123456"); // 手机和蓝牙采集器配对
                    boolean bBond = createBond(btDevice.getClass(), btDevice);
                    boolean bConfirm = setPairingConfirmation(btDevice.getClass(), btDevice);
                    boolean bCancel = cancelPairingUserInput(btDevice.getClass(), btDevice); //某些手机在此处会抛出异常，若果有需要处理的可在catch中处理
                    mHandler.sendMessage(mHandler.obtainMessage(0, "bPin:"+bPin+", bBond:"+bBond+", bConfirm:"+bConfirm+", bCancel:"+bCancel));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
