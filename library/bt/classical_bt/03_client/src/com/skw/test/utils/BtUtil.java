package com.skw.test.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import java.lang.reflect.Method;
public class BtUtil {

    /**
     * 读取蓝牙的类型(单模/双模)
     */
    public static String getDeviceType(BluetoothDevice device) {
        switch (device.getType()) {
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                return "unknow";
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                return "classic";
            case BluetoothDevice.DEVICE_TYPE_LE:
                return "BLE";
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                return "DUAL";
        }

        return "unkown";
    }

    /**
     * 获取绑定状态
     */
    public static String getBondState(BluetoothDevice device) {
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_NONE:
                return "none";
            case BluetoothDevice.BOND_BONDING:
                return "bonding";
            case BluetoothDevice.BOND_BONDED:
                return "bouned";
        }

        return "none";
    }

    /**
     * 读取支持的UUID服务
     */
    public static String getUuids(BluetoothDevice device) {
        StringBuilder sb = new StringBuilder();
        ParcelUuid[] pUuids = device.getUuids();
        if (pUuids != null) {
            for (ParcelUuid pUuid: pUuids) {
                String uuid = pUuid.toString();
                sb.append("\n").append(uuid).append("  (").append(BtUuidUtil.getAttributeName(uuid, "unknown")).append(")");
            }
        }

        return sb.toString();
    }


    public static boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        Method setPinMethod = btClass.getDeclaredMethod("setPin", new Class[]{ byte[].class});
        Boolean returnValue = (Boolean) setPinMethod.invoke(btDevice, new Object[]{str.getBytes()});
        return returnValue.booleanValue();
    }

    public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    public static boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }


    public static boolean setPairingConfirmation(Class btClass, BluetoothDevice device) throws Exception {
        Boolean returnValue = (Boolean) device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
        return returnValue.booleanValue();
    }

    public static boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
        Boolean returnValue = (Boolean) device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
        return returnValue.booleanValue();
    }

}
