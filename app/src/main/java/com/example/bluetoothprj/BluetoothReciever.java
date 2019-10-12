package com.example.bluetoothprj;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class BluetoothReciever extends BroadcastReceiver {

    private static final String TAG = "BluetoothReciever";
    BluetoothDevice device;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        if (TextUtils.equals(action, BluetoothAdapter.ACTION_DISCOVERY_STARTED)) { //开启搜索
//            if (onDeviceSearchListener != null) {
//                onDeviceSearchListener.onDiscoveryStart();  //开启搜索回调
//            }
//
//        } else if (TextUtils.equals(action, BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {//完成搜素
//            if (onDeviceSearchListener != null) {
//                onDeviceSearchListener.onDiscoveryStop();  //完成搜素回调
//            }
//
//        } else if (TextUtils.equals(action, BluetoothDevice.ACTION_FOUND)) {  //3.0搜索到设备
//            //蓝牙设备
//            BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//            //信号强度
//            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
//
//            Log.d(TAG, "扫描到设备：" + bluetoothDevice.getName() + "-->" + bluetoothDevice.getAddress());
//            if (onDeviceSearchListener != null) {
//                onDeviceSearchListener.onDeviceFound(bluetoothDevice,rssi);  //3.0搜素到设备回调
//            }
//        }


        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
             device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address

            Log.d(TAG, "onReceive: 发现了这些设备_________\n"+deviceName+"_________\n"+deviceHardwareAddress+"_________\n");
        }


        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.

            Log.d(TAG, "onReceive: "+"结束扫描_________\n");
        }



        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.

            Log.d(TAG, "onReceive: "+"开始扫描_________\n");
        }




    }


    /**
     * 蓝牙设备搜索监听者
     * 1、开启搜索
     * 2、完成搜索
     * 3、搜索到设备
     */
    public interface OnDeviceSearchListener {
        void onDiscoveryStart();   //开启搜索
        void onDiscoveryStop();    //完成搜索
        void onDeviceFound(BluetoothDevice bluetoothDevice, int rssi);  //搜索到设备
    }

    private OnDeviceSearchListener onDeviceSearchListener;

    public void setOnDeviceSearchListener(OnDeviceSearchListener onDeviceSearchListener) {
        this.onDeviceSearchListener = onDeviceSearchListener;
    }




}