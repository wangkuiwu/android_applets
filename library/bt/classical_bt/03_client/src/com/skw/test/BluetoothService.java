package com.skw.test;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Binder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.skw.test.util.BtUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothService extends Service {
    private static final String TAG = "##skywang-BluetoothService";

    // SPP
	private static final UUID S_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST"; 

    private long mConnectTimeStart;     // 连接开始的时间
    private long mConnectTimeEnd;       // 连接完成的时间

    private Handler mBluetoothHandler;
    private BluetoothAdapter mBluetoothAdapter = null;
    private ArrayList<BluetoothDevice> mScanList = new ArrayList<BluetoothDevice>();
    private Set<BluetoothDevice> mBondedDevices;
    // 绑定的蓝牙设备
    private BluetoothSocket mBluetoothSocket;
    // 绑定的蓝牙设备的Socket
    private BluetoothDevice mBluetoothDevice;
    // 连接蓝牙的线程
    private ConnectThread mConnectThread;
    // 蓝牙通信(读写)的线程
    private ConnectedThread mConnectedThread;
    // 自动连接/配对/读写进行
    private AutoThread mAutoThread;

    private final IBinder mBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        // 监听蓝牙扫描广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); 
        filter.addAction(ACTION_PAIRING_REQUEST); 
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);

        // 初始化蓝牙
        initBt();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(mReceiver);
        disconnectBluetooth();
    }

    private void initBt() {
        // 获取默认的BluetoothAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            mBondedDevices = mBluetoothAdapter.getBondedDevices();
        }
    }

    /**
     * 检查BT是否打开，没有打开的话，则弹出打开BT确认对话框
     */
    public boolean isBluetoothEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * 读取已绑定的蓝牙列表
     */
    public Set<BluetoothDevice> readBondedDevices() {
        return mBondedDevices;
    }

    /**
     * 设置蓝牙的消息处理器(例如，扫描消息)
     */
    public void setBluetoothHandler(Handler handler) {
        mBluetoothHandler = handler;
    }

    /**
     * 蓝牙是否处于扫描状态
     */
    public boolean isDiscovering() {
        return mBluetoothAdapter.isDiscovering();
    }

    /**
     * 开始扫描
     */
    public void doBluetoothDiscovery(boolean flag) {
        // 若已经在扫描中，则先停止扫描
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        if (flag) {
            // 清空上一次扫描的列表
            mScanList.clear();
            // 开始扫描
            mBluetoothAdapter.startDiscovery();
        }
    }

    /**
     * BT扫描广播
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) { // 扫描到蓝牙设备的广播
                // 获取扫描到的BT设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ((!mScanList.contains(device)) && (!mBondedDevices.contains(device))) {
                    mScanList.add(device);
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_DEVICE_FOUNT, device)); // 发送消息
                }
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) { // 绑定状态发生变化
                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                Log.d(TAG, "BT State Changed to "+state);
                mHandler.sendEmptyMessage(MSG_BT_BOND_STATE_CHANGED);
            } else if (action.equals(ACTION_PAIRING_REQUEST)) { // 绑定请求对话框
                Log.d(TAG, "BT receive: "+ACTION_PAIRING_REQUEST);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                autoBond(device);
                mConnectTimeEnd = System.currentTimeMillis();
                mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_CONNECT_FINISHED, (int)(mConnectTimeEnd-mConnectTimeStart), 2, isBluetoothConnected()));
                Log.d(TAG, "autoBond finish, connection="+isBluetoothConnected()+" time:"+(mConnectTimeEnd-mConnectTimeStart)+"ms");
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) { // 扫描结束
                mHandler.sendEmptyMessage(MSG_BT_DISCOVERY_FINISHED);
            } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) { // 扫描到蓝牙设备的广播
                int currState = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE);
                // 蓝牙被打开
                if (currState == BluetoothAdapter.STATE_ON) {
                    Log.d(TAG, "bluetooth is on");
                    initBt();
                } else if (currState == BluetoothAdapter.STATE_OFF) {
                    Log.d(TAG, "bluetooth is off");
                    doBluetoothDiscovery(false);
                }
            }
        }
    };

    // ================================ 蓝牙数据传输 ================================

    /**
     * 读取蓝牙的信息
     */
    public String readBluetoothInfo(String address) {
        StringBuilder sb = new StringBuilder();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        if (device != null) {
            // 读取蓝牙的名称
            sb.append("==1== name: ").append(device.getName());
            // 读取蓝牙的地址
            sb.append("\n==2== mac: ").append(device.getAddress());
            // 读取蓝牙的类型(单模/双模)
            sb.append("\n==3== type: ").append(BtUtil.getDeviceType(device));
            // 读取蓝牙的绑定状态
            sb.append("\n==4== BondState: ").append(BtUtil.getBondState(device));
            // 读取蓝牙的连接状态
            sb.append("\n==5== Connected: ").append(isBluetoothConnected());
            // 读取蓝牙支持的服务UUID
            String uuids = BtUtil.getUuids(device);
            if (TextUtils.isEmpty(uuids)) {
                sb.append("\n==6== uuid: NONE");
            } else {
                sb.append("\n==6== uuid: ").append(uuids);
            }
        }

        return sb.toString();
    }

    /**
     * 判断蓝牙是否绑定
     */
    public boolean isBluetoothBonded(final String address) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        return device.getBondState() == BluetoothDevice.BOND_BONDED;
    }

    /**
     * 绑定蓝牙
     */
    public void bondBluetooth(final String address) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        // 如果没有绑定，则进行绑定
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            new BondThread(device).start();
        }
    }

    /**
     * 解除绑定蓝牙
     */
    public void unbondBluetooth(final String address) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        // 如果已经绑定，则进行解除绑定
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            new UnbondThread(device).start();
        }
    }

    /**
     * 自动绑定
     */
    private void autoBond(BluetoothDevice device) {
        try {
            boolean bConfirm = BtUtil.setPairingConfirmation(device.getClass(), device);
            boolean bCancel = BtUtil.cancelPairingUserInput(device.getClass(), device); //某些手机在此处会抛出异常，若果有需要处理的可在catch中处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDataTransfer() {
        return mWriteThread!=null;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(S_UUID);
        } catch (Exception e) {
            Log.e(TAG, "create socket failed", e);
        }

        return tmp;
    }

    public boolean isBluetoothConnected() {
        return (mBluetoothSocket!=null && mBluetoothSocket.isConnected());
    }

    public void connectBluetooth(String address) {
        Log.d(TAG, "connect bluetooth:"+address);
        if (mConnectThread == null) {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            mBluetoothSocket = createBluetoothSocket(device);
            mConnectThread = new ConnectThread(device, mBluetoothSocket);
            mConnectThread.start();
        }
    }

    public void disconnectBluetooth() {
        long startTime = System.currentTimeMillis();
        stopContiniusWrite();

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        long endTime = System.currentTimeMillis();
        mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_DISCONNECT_FINISHED, (int)(endTime-startTime), 0, isBluetoothConnected()));
    }

    /**
     * 通过蓝牙发送消息
     */
    public void sendBluetoothMessage(String text) {
        if (mConnectedThread!=null && (!TextUtils.isEmpty(text))) {
            mConnectedThread.write(text.getBytes());
        }
    }

    /**
     * 绑定蓝牙的线程
     */
    private final class BondThread extends Thread {
        private final BluetoothDevice mmDevice;

        public BondThread(final BluetoothDevice device) {
            mmDevice = device;
        }

        @Override
        public void run() {
            try {
                // 如果没有绑定，则先绑定
                if (mmDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    // 发送消息
                    mHandler.sendEmptyMessage(MSG_BT_BOND_START);
                    Log.i("TAG", "start bounding");
                    BtUtil.createBond(mmDevice.getClass(), mmDevice);
                }
            } catch (Exception e) {
                Log.e(TAG, "bound failed", e);
            }
            mHandler.sendEmptyMessage(MSG_BT_BOND_FINISHED);
            Log.i(TAG, "after bond, bonded="+(mmDevice.getBondState()==BluetoothDevice.BOND_BONDED));
        }
    }

    /**
     * 解除绑定蓝牙的线程
     */
    private final class UnbondThread extends Thread {
        private final BluetoothDevice mmDevice;

        public UnbondThread(final BluetoothDevice device) {
            mmDevice = device;
        }

        @Override
        public void run() {
            try {
                // 如果没有绑定，则先绑定
                if (mmDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    mHandler.sendEmptyMessage(MSG_BT_UNBOND_START);
                    Log.i(TAG, "start unbond");
                    BtUtil.removeBond(mmDevice.getClass(), mmDevice);
                }
            } catch (Exception e) {
                Log.e(TAG, "bound failed", e);
            }
            mHandler.sendEmptyMessage(MSG_BT_UNBOND_FINISHED);
            Log.i(TAG, "after unbond, unbonded="+(mmDevice.getBondState()==BluetoothDevice.BOND_NONE));
        }
    }


    /**
     * 连接蓝牙的线程
     */
    private final class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(final BluetoothDevice device, final BluetoothSocket socket) {
            mmDevice = device;
            mmSocket = socket;
        }

        @Override
        public void run() {
            int retries = 0;
            mConnectTimeStart = System.currentTimeMillis();
            mHandler.sendEmptyMessage(MSG_BT_CONNECT_START);
            // 如果正在扫描，则先取消扫描
            mBluetoothAdapter.cancelDiscovery();
            // 尝试连接10次
            while (retries++ < 10) {
                try {
                    mmSocket.connect();
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
            mConnectTimeEnd = System.currentTimeMillis();
            mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_CONNECT_FINISHED, (int)(mConnectTimeEnd-mConnectTimeStart), 1, isBluetoothConnected()));
            Log.d(TAG, "connect finish, retries="+retries+", connection="+mmSocket.isConnected()+" time:"+(mConnectTimeEnd-mConnectTimeStart)+"ms");
            // 防止"配对对话框没有关闭"，这里再调用一次autoBond()
            autoBond(mmDevice);

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
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_MSG_RECEIVE, new String(buffer)));
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
                mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_MSG_WRITE, new String(buffer)));
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
    public void startContiniusWrite(int unit, int loop, int interval) {
        if (mWriteThread==null) {
            mWriteThread = new WriteThread(mBluetoothSocket, unit, loop, interval);
            mWriteThread.start();
        }
    }

    public void stopContiniusWrite() {
        if (mWriteThread != null) {
            mWriteThread.interrupt();
            mWriteThread = null;
        }
        mHandler.sendEmptyMessage(MSG_BT_WRITE_STOP);
    }

    private class WriteThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;
        private long mIndex = 0;
        private long mCount = 0;

        private final int mLoop;
        private final int mUnit;
        private final int mInterval;

        public WriteThread(BluetoothSocket socket, int unit, int loop, int interval) {
            mUnit = unit;
            mLoop = loop;
            mInterval = interval;
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

        private byte[] getData(int unit) {
            int kUnit = 1024*unit;
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<kUnit; i++) {
                sb.append("1");
            }
            return sb.toString().getBytes();
        }

        @Override
        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = getData(mUnit);
            long startTime = System.currentTimeMillis();
            long prevTime = startTime;
            mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_WRITE_START, "unit:"+mUnit+"(KB)"+", count: "+mLoop+", interval:"+mInterval+"ms\n"));

            // 不断监听输入(即服务器发过来的数据)
            while (mIndex < mLoop) {
                try {
                    mIndex++;
                    // 从InputStream中读取数据
                    mmOutStream.write(buffer);
                    // 将数据显示出来
                    long currTime = System.currentTimeMillis();
                    StringBuilder sb = new StringBuilder();
                    mCount += buffer.length;
                    sb.append("write-").append(mIndex)
                        .append(": tSize=").append(mCount).append("KB")
                        .append(" tTime=").append(currTime-startTime).append("ms")
                        .append("\n");
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_WRITE_INFO, sb.toString()));
                    prevTime = currTime;

                    if (mInterval > 0) {
                        sleep(mInterval);
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, "interupptet finished", e);
                    break;
                } catch (IOException e) {
                    Log.e(TAG, "disconnected finished", e);
                    break;
                }
            }
            mHandler.sendEmptyMessage(MSG_BT_WRITE_FINISHED);
        }
    }

    public void runAutoThread(final String address) {
        Log.d(TAG, "runAutoThread address="+address);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_BT_AUTO_RUN, address), 5000);
    }

    public void doRunAutoThread(final String address) {
        Log.d(TAG, "doRunAutoThread address="+address);
        mAutoThread = new AutoThread(address);
        mAutoThread.start();
    }

    /**
     * 自动连接并发送数据
     */
    private final class AutoThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public AutoThread(final String address) {
            mmDevice = mBluetoothAdapter.getRemoteDevice(address);
            mmSocket = createBluetoothSocket(mmDevice);
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            Log.d(TAG, "create AutoThread: name="+mmDevice.getName()+"+ mmDevice="+mmDevice+", mmSocket="+mmSocket);
        }

        @Override
        public void run() {
            int retries = 0;
            mConnectTimeStart = System.currentTimeMillis();
            // 如果正在扫描，则先取消扫描
            mBluetoothAdapter.cancelDiscovery();
            // 尝试连接10次
            while (retries++ < 10) {
                try {
                    mmSocket.connect();
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
            mConnectTimeEnd = System.currentTimeMillis();
            Log.d(TAG, "autoThread finish, connected="+mmSocket.isConnected()+" time:"+(mConnectTimeEnd-mConnectTimeStart)+"ms");

            if (mmSocket.isConnected()) {
                String text = "message from AutoMessage";
                write(text.getBytes());
                Log.d(TAG, "write message="+text);
            } else {
                Log.d(TAG, "unable to write message!");
            }
        }

        public void write(byte[] buffer) {
            try {
                // 将数据写入到OutputStream中
                mmOutStream.write(buffer);
                // mHandler.sendMessage(mHandler.obtainMessage(MSG_BT_AUTO_WRITE, new String(buffer)));
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        private void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    public static final int MSG_BT_DEVICE_FOUNT             = 0x1;
    public static final int MSG_BT_DISCOVERY_FINISHED       = 0x2;
    public static final int MSG_BT_BOND_STATE_CHANGED       = 0x10;
    public static final int MSG_BT_BOND_START               = 0x11;
    public static final int MSG_BT_BOND_FINISHED            = 0x12;
    public static final int MSG_BT_UNBOND_START             = 0x13;
    public static final int MSG_BT_UNBOND_FINISHED          = 0x14;
    public static final int MSG_BT_CONNECT_CHANGED          = 0x20;
    public static final int MSG_BT_CONNECT_START            = 0x21;
    public static final int MSG_BT_CONNECT_FINISHED         = 0x22;
    public static final int MSG_BT_DISCONNECT_START         = 0x23;
    public static final int MSG_BT_DISCONNECT_FINISHED      = 0x24;
    public static final int MSG_BT_MSG_RECEIVE              = 0x31;
    public static final int MSG_BT_MSG_WRITE                = 0x32;
    public static final int MSG_BT_AUTO_RUN                 = 0x41;
    public static final int MSG_BT_WRITE_START              = 0x51;
    public static final int MSG_BT_WRITE_INFO               = 0x52;
    public static final int MSG_BT_WRITE_FINISHED           = 0x53;
    public static final int MSG_BT_WRITE_STOP               = 0x54;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_BT_AUTO_RUN: {
                    doRunAutoThread((String)msg.obj);
                    break;
                }
                case MSG_BT_DEVICE_FOUNT: {
                    BluetoothDevice device = (BluetoothDevice)msg.obj;
                    Log.d(TAG, "found device="+device);
                    // 外发消息
                    if (mBluetoothHandler != null) {
                        // mBluetoothHandler.sendMessage(mBluetoothHandler.obtainMessage(msg.what, device));
                        mBluetoothHandler.sendMessage(Message.obtain(msg));
                    }
                    break;
                }
                case MSG_BT_MSG_RECEIVE:
                case MSG_BT_MSG_WRITE: {
                    String text = (String)msg.obj;
                    // 外发消息
                    if (mBluetoothHandler != null) {
                        mBluetoothHandler.sendMessage(Message.obtain(msg));
                    }
                    break;
                }
                case MSG_BT_CONNECT_FINISHED: {
                    int millis = msg.arg1;
                    if (mBluetoothHandler != null) {
                        mBluetoothHandler.sendMessage(Message.obtain(msg));
                    }
                    break;
                }
                case MSG_BT_DISCOVERY_FINISHED: // 扫描结束
                case MSG_BT_BOND_START: // 绑定开始
                case MSG_BT_BOND_FINISHED: // 绑定结束
                case MSG_BT_UNBOND_START: // 解除绑定开始
                case MSG_BT_UNBOND_FINISHED: // 解除绑定结束
                default:
                    if (mBluetoothHandler != null) {
                        mBluetoothHandler.sendMessage(Message.obtain(msg));
                    }
                    break;
            }
        }
    };
}
