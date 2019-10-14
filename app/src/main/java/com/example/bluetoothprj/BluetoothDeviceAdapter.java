package com.example.bluetoothprj;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class BluetoothDeviceAdapter implements Runnable {
    private static final String TAG = "BluetoothDeviceAdapter";

    protected Context context;

    int uuid = BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE;
    protected UUID MY_UUID = UUID
            .fromString("00001800-0000-1000-8000-00805f9b34fb");
    protected Map<String, String> measureResult;
    protected BluetoothDevice bluetoothDevice;
    protected BluetoothAdapter bluetoothAdapter;
    protected static MainActivity.BluetoothReciever bluetoothReceiver;
    protected BluetoothSocket socket;
    protected Handler handlerBluetooth;
    protected InputStream inputStream;
    protected OutputStream outputStream;

    BluetoothSocket tmp = null;


    /**
     * 构造方法:获取本地蓝牙实例，打开蓝牙，搜索设备信息，查询已配对的设备
     *
     * @throws IOException
     */
    public BluetoothDeviceAdapter(Context context, BluetoothDevice device) {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        this.context = context;
        bluetoothDevice = device;


        //1、获取BluetoothSocket
        try {
            //建立安全的蓝牙连接，会弹出配对框
            tmp = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("0000111e-0000-1000-8000-00805f9b34fb"));

        } catch (IOException e) {
            Log.e(TAG, "ConnectThread-->获取BluetoothSocket异常!" + e.getMessage());
        }


    }




    private  InputStream mmInStream;
    private  OutputStream mmOutStream;

    private Handler handler;

    private byte[] mmBuffer;


    /**
     * 线程体：执行连接和读取数据
     */
    @Override
    public void run() {
        Log.w("run()", "线程体：执行连接和读取数据");
        // TODO Auto-generated method stub

//        try {
        bluetoothAdapter.cancelDiscovery();
        connect();
//            readData();
//        } catch (IOException e) {
//            measureResult.put("errorInfo", e.getMessage());
//        }

        Log.i("AbstractedAdapter", "run()");

    }


    /**
     * 请求与服务端建立连接
     */
    private void connect() {


        Log.w("connect()", "请求与服务端建立连接");
        // 客户端bluetoothDevice请求与Server建立连接socket

        bluetoothAdapter.cancelDiscovery();


        socket = tmp;
        if (socket != null) {
            Log.w(TAG, "ConnectThread-->已获取BluetoothSocket");
        }


        //2、通过socket去连接设备
        try {
            Log.d(TAG, "ConnectThread:run-->去连接...");

            socket.connect();  //connect()为阻塞调用，连接失败或 connect() 方法超时（大约 12 秒之后），它将会引发异常

            Log.d(TAG, "connect: 连接成功？？？？");

        } catch (IOException e) {
            Log.e(TAG, "ConnectThread:run-->连接异常！" + e.getMessage());



            //释放
            destory();
        }









    }







    /**
     * 关闭输入输出流，释放连接，关闭蓝牙
     */
    protected void destory() {
        Log.w("destory()", "关闭输入输出流，释放连接，关闭蓝牙");
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }
            /*
             * if (bluetoothAdapter != null) { bluetoothAdapter.disable(); }
             */

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}