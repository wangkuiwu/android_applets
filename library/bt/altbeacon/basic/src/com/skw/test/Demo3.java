package com.skw.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.TextView;
      
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * AltBeacon 的MonitorNotifier和RangeNotifier的测试程序
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class Demo3 extends Activity implements BeaconConsumer {
    private static final String TAG = "##skywang-Demo3";

    private TextView mTvInfo;
    private MyAdapter mAdapter;
    private ListView mListView;

    private BeaconManager mBeaconManager;
    private Region mRangeRegion = new Region("demo3_RangeRegion", null, null, null);
    private Region mMonitorRegion = new Region("demo3_MonitorRegion", null, null, null);

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTvInfo.append((String)msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo3);

        // 获取BeaconManager对象
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        // 设置监听的Beacon的类型。这里的"m:2-3=0215"是苹果设备(iPhone, iPad作为Beacon基站)的前缀。
        // (1) m:2-3=0215   第2-3个字节，表示Beacon基站的prefix。苹果设备作为基站时的prefix固定是0215
        // (2) i:4-19       第4-19个字节，表示UUID(即独立的设备ID)。
        // (3) i:20-21      第20-21个字节，表示major
        // (4) i:22-23      第22-23个字节，表示minor
        // (5) p:24-24      第24个字节，表示RSSI。信号强度or距离
        // (6) d:25-25      第25个字节，data field。
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager.setMonitorNotifier(new MyMonitorNotifier());     // 设置"进入/退出区域"的监视器
        mBeaconManager.setRangeNotifier(new MyRangeNotifier());         // 设置"RangeNotifier"的监视器
        mBeaconManager.bind(this);                                      // 绑定到BeaconService服务

        mTvInfo = (TextView) findViewById(R.id.tv_info);
        ToggleButton tglButton = (ToggleButton) findViewById(R.id.tgl);
        tglButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i(TAG, "bind Beacon");
                    // 绑定到BeaconService服务
                    mBeaconManager.bind(Demo3.this);
                } else {
                    Log.i(TAG, "unbind Beacon");
                    // 与BeaconService服务解除绑定
                    mBeaconManager.unbind(Demo3.this);
                }
            }
        }); 

        // 创建ListView的适配器
        mAdapter = new MyAdapter(this);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter); 
    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();
        // 与BeaconService服务解除绑定
        mBeaconManager.unbind(this);
    }

    /**
     * 当前Activity连上BeaconService服务时的回调函数
     */
    @Override
    public void onBeaconServiceConnect() {
        try {
            mBeaconManager.startMonitoringBeaconsInRegion(mMonitorRegion);
            mBeaconManager.startRangingBeaconsInRegion(mRangeRegion);
        } catch (RemoteException e) {   }   
    }

    private class MyMonitorNotifier implements MonitorNotifier {
        @Override
        public void didEnterRegion(Region region) {
            Log.i(TAG, "I see an beacon");
            mHandler.sendMessage(mHandler.obtainMessage(0, new String("\ndidExitRegion region="+region)));
        }

        @Override
        public void didExitRegion(Region region) {
            Log.i(TAG, "I no longer see an beacon");
            mHandler.sendMessage(mHandler.obtainMessage(1, new String("\ndidExitRegion region="+region)));
        }

        @Override
        public void didDetermineStateForRegion(int state, Region region) {
            Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);        
            mHandler.sendMessage(mHandler.obtainMessage(1, new String("\ndidDetermineStateForRegion Region="+region+", state="+state)));
        }
    }

    private class MyRangeNotifier implements RangeNotifier {
        @Override
        public void didRangeBeaconsInRegion(final Collection<Beacon> rangedBeacons, Region region) {
            Log.i(TAG, "range: beacons="+rangedBeacons.size()+", region="+region.getUniqueId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setData(rangedBeacons);
                }
            });
        }
    }

    public class MyAdapter extends BaseAdapter {
        public class MyComparator implements Comparator<Beacon> {
            @Override
            public int compare(Beacon beacon1, Beacon beacon2) {
                return Double.compare(beacon1.getDistance(), beacon2.getDistance());
            }
        }

        private List<Beacon> mBeaconList = Collections.synchronizedList(new ArrayList<Beacon>());
        private LayoutInflater mInflator;

        public MyAdapter(Context context) {
            mInflator = LayoutInflater.from(context);
        }

        public void setData(Collection<Beacon> beacons) {
            mBeaconList.clear();
            mBeaconList.addAll(beacons);
            Collections.sort(this.mBeaconList, new MyComparator());
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mBeaconList.size();
        }

        @Override
        public Beacon getItem(int position) {
            return mBeaconList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                v = mInflator.inflate(R.layout.demo3_list_item, null);
            }

            Beacon beacon = getItem(position);
            TextView mac = (TextView) v.findViewById(R.id.macAddress);
            TextView uuid = (TextView) v.findViewById(R.id.uuidNumber);
            TextView major = (TextView) v.findViewById(R.id.majorNumber);
            TextView minor = (TextView) v.findViewById(R.id.minorNumber);
            TextView distance = (TextView) v.findViewById(R.id.distance);

            mac.setText(beacon.getBluetoothAddress());
            uuid.setText(beacon.getId1().toString());
            major.setText(beacon.getId2().toString());
            minor.setText(beacon.getId3().toString());
            distance.setText(new DecimalFormat("#0.000").format(beacon.getDistance()));

            return v;
        }
    }
}
