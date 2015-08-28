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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * BT(2.0) 客户端与服务器通信示例。
 *
 * 该示例是服务端程序
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class MainActivity extends Activity {
    private static final String TAG = "##skywang-Server";
    private static final int REQUEST_ENABLE_BT = 1;
	private static final UUID S_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String mMacAddress;
    private EditText mEtMsg;
    private TextView mTvMac;
    private TextView mTvInfo;
    private BluetoothAdapter mBluetoothAdapter = null;

  	private RequestAcceptThread mServerThread = null;
  	private ConnectedThread mConnectedThread = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTvInfo.append("\n"+(String)msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 初始化蓝牙
        initBt();

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

        // 显示消息的TextView
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mTvInfo.setText("Not Connected!");

        ToggleButton tglButton = (ToggleButton) findViewById(R.id.tgl);
        tglButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i(TAG, "start listen");
                    startListen();
                } else {
                    Log.i(TAG, "stop listen");
                    stopListen();
                }
            }
        }); 

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

    private void startListen() {
        mServerThread = new RequestAcceptThread();
        mServerThread.start();
    }

    private void stopListen() {
  	    if (mServerThread != null) {
            Log.d(TAG, "interrupt server thread");
            mServerThread.interrupt();
            mServerThread = null;
        }

  	    if (mConnectedThread != null) {
            Log.d(TAG, "interrupt connect thread");
            mConnectedThread.interrupt();
            mConnectedThread = null;
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
        stopListen();
    }

  	private final class RequestAcceptThread extends Thread {
        private BluetoothServerSocket mBluetoothServerSocket = null;
  		
  		public RequestAcceptThread() {
            boolean bSuccess = false;
            BluetoothServerSocket tmp = null;

            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("bt_test_chat", S_UUID);
                bSuccess = true;
            } catch (IOException e) {
                Log.e(TAG, "listen failed", e);
            }
  			mBluetoothServerSocket = tmp;
            mHandler.sendMessage(mHandler.obtainMessage(0, bSuccess ? "listen to socket" : "failed listen to socket!"));
  		}

  		@Override
  		public void run() {
  			super.run();

  			BluetoothSocket socket = null;
  			while(true) {
  				try{
                    mHandler.sendMessage(mHandler.obtainMessage(0, "waiting..."));
                    // 监听客户端的连接请求。这里是阻塞式监听！
  					socket = mBluetoothServerSocket.accept();
                    break ;
                } catch (Exception e) {
                    mHandler.sendMessage(mHandler.obtainMessage(0, "accept exception"));
                    Log.e(TAG, "accept Error, socket:"+socket, e);
  				}
  			}

            if(socket != null) {
                mHandler.sendMessage(mHandler.obtainMessage(0, "connected success!"));
                mConnectedThread = new ConnectedThread(socket);
                mConnectedThread.start();
            }
  		}

  		public void cancel() {
  			try {
  				mBluetoothServerSocket.close();
  			} catch ( IOException e) {
                Log.e(TAG, "cancel socket failed", e);
            }
  		}
  	}

	private final class ConnectedThread extends Thread {
        private OutputStream mOutputStream;
        private InputStream mInputStream;
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "connect thread error", e);
            }
            
            mInputStream = tmpIn;
            mOutputStream= tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024]; // buffer store for the stream
            int bytes; // bytes returned from read()

  			while(true) {
                try {
                    bytes = mInputStream.read(buffer);
                    mHandler.sendMessage(mHandler.obtainMessage(5, "RECEIVE: "+new String(buffer)));
                } catch (IOException e) {
                    Log.e(TAG, "connect thread run error", e);
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                // 将数据写入到OutputStream中
                mOutputStream.write(buffer);
                // 显示发送数据
                mHandler.sendMessage(mHandler.obtainMessage(0, "SEND: "+new String(buffer)));
            } catch (IOException e) {
                Log.e(TAG, "connect thread write error", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "connect thread cancel error", e);
            }
        }
    }
}
