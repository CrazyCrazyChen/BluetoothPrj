package com.example.bluetoothprj;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
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
    protected UUID MY_UUID = UUID
            .fromString("00001800-0000-1000-8000-00805f9b34fb");
    protected Map<String, String> measureResult;
    protected BluetoothDevice bluetoothDevice;
    protected BluetoothAdapter bluetoothAdapter;
    protected static BluetoothReciever bluetoothReceiver;
    protected BluetoothSocket socket;
    protected Handler handlerBluetooth;
    protected InputStream inputStream;
    protected OutputStream outputStream;


    BluetoothSocket tmp = null ;

    public int dataLeng = 32;
    private byte dataHead = (byte) 0x0A;
    private String DEVICE_NAME = "酷狗蓝牙耳机 M1";

    /**
     * 构造方法:获取本地蓝牙实例，打开蓝牙，搜索设备信息，查询已配对的设备
     *
     * @param handler
     * @throws IOException
     */
    public BluetoothDeviceAdapter(Context context, Handler handler) {
        Log.w("BluetoothDeviceAdapter", "获取本地蓝牙实例，打开蓝牙，搜索设备信息，查询已配对的设备");


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        openBluetoothAdapter();
        Log.d(TAG, "BluetoothDeviceAdapter------------------------: 蓝牙开启成功");




        queryPairedDevicesInfo();
        Log.d(TAG, "BluetoothDeviceAdapter------------------------: 蓝牙查询成功");


        this.context = context;
        this.handlerBluetooth = handler;



        registerAndDiscover();


        measureResult = new HashMap<String, String>();




        //1、获取BluetoothSocket
        try {
            //建立安全的蓝牙连接，会弹出配对框
            tmp = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

        } catch (IOException e) {
            Log.e(TAG,"ConnectThread-->获取BluetoothSocket异常!" + e.getMessage());
        }


    }

    /**
     * 根据当前本地蓝牙适配器的状态选择性询问用户启动它
     */
    protected void openBluetoothAdapter() {
        Log.w("openBluetoothAdapter()", "打开本地蓝牙" + bluetoothAdapter.getName());
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
                Log.i("openBluetoothAdapter", "当前状态为关闭，系统自动打开");
            }

        } else {
            Log.i("openBluetoothAdapter()", "本地设备驱动异常!");
        }
    }


//    public int getCount(){
//        return 0;
//    }
    /**
     * 注册广播事件监听器，并开始扫描发现设备
     */
    private void registerAndDiscover() {
        Log.w("registerScan()", "注册广播事件并准备扫描发现周边设备");
//        bluetoothReceiver = new BluetoothReciever();
//        IntentFilter infilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        context.registerReceiver(bluetoothReceiver, infilter);
//        if (bluetoothAdapter.startDiscovery()) {
//            Log.i("bluetoothAdapter", "开始扫描");
//        }




      //  BluetoothReciever bluetoothReciever= new BluetoothReciever();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到设备
        context.registerReceiver(bluetoothReceiver,intentFilter);
        Log.d(TAG, "onCreate: sh设备绑定-----------------成功");


        startScanBluth();

//
//        bluetoothReceiver = new BluetoothReciever();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描
//        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
//        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到设备
//        context.registerReceiver(bluetoothReceiver,intentFilter);


    }

    /**
     * 查询已配对的设备
     */
    public void queryPairedDevicesInfo() {
        // 通过getBondedDevices方法来获取已经与本设备配对的远程设备信息列表
        Log.w("queryPairedDevicesInfo", "查询已配对的设备");
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter
                .getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.i("已配对的设备名称", device.getName());
                Log.i("已配对的设备地址", device.getAddress());
                // 查找已配对的，按此目标创建远程bluetoothDevice
                if (DEVICE_NAME.equals(device.getName())) {

                    //已经存在的特定的蓝牙设备


                    Log.w("发现目标设备，按此创建远程端", DEVICE_NAME);
                    bluetoothDevice = device;
                    break;
                }
            }
        }
        if (bluetoothDevice == null)
            Log.i("queryPairedDevices2()", "没有与目标远程端配对的信息");
    }



    private void startScanBluth() {
        // 判断是否在搜索,如果在搜索，就取消搜索
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        // 开始搜索
        bluetoothAdapter.startDiscovery();

        ProgressDialog progressDialog = new ProgressDialog(context);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setMessage("正在搜索，请稍后！");
        progressDialog.show();

        final ProgressDialog finalProgressDialog = progressDialog;
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finalProgressDialog.dismiss();

                bluetoothAdapter.cancelDiscovery();

            }
        }.start();


    }




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
        Message msg = handlerBluetooth.obtainMessage();
        msg.obj = this.measureResult;
        handlerBluetooth.sendMessage(msg);
        Log.i("AbstractedAdapter", "run()");

    }



    /**
     * 请求与服务端建立连接
     */
    private void connect() {




        Log.w("connect()", "请求与服务端建立连接");
        // 客户端bluetoothDevice请求与Server建立连接socket




//        try {
//            socket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
//            socket.connect();
//            if (socket.isConnected()) {
//                Log.i("connect()", "成功连接");
//            }
//            inputStream = socket.getInputStream();
//        } catch (IOException e) {
//            Log.e("connect()", "连接异常");
//            destory();
//        }





        bluetoothAdapter.cancelDiscovery();




        socket = tmp;
        if(socket != null){
            Log.w(TAG,"ConnectThread-->已获取BluetoothSocket");
        }



        //2、通过socket去连接设备
        try {
            Log.d(TAG,"ConnectThread:run-->去连接...");
//            if(onBluetoothConnectListener != null){
//                onBluetoothConnectListener.onStartConn();  //开始去连接回调
//            }
            socket.connect();  //connect()为阻塞调用，连接失败或 connect() 方法超时（大约 12 秒之后），它将会引发异常

            Log.d(TAG, "connect: 连接成功？？？？");
//            if(onBluetoothConnectListener != null){
//                onBluetoothConnectListener.onConnSuccess(socket);  //连接成功回调
//                Log.w(TAG,"ConnectThread:run-->连接成功");
//            }

        } catch (IOException e) {
            Log.e(TAG,"ConnectThread:run-->连接异常！" + e.getMessage());

//            if(onBluetoothConnectListener != null){
//                onBluetoothConnectListener.onConnFailure("连接异常：" + e.getMessage());
//            }


            //释放
            cancel();
        }

    }

    /**
     * 释放
     */
    public void cancel() {
        try {
            if (socket != null && socket.isConnected()) {
                Log.d(TAG,"ConnectThread:cancel-->mmSocket.isConnected() = " + socket.isConnected());
                socket.close();
                socket = null;
                return;
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }

            Log.d(TAG,"ConnectThread:cancel-->关闭已连接的套接字释放资源");

        } catch (IOException e) {
            Log.e(TAG,"ConnectThread:cancel-->关闭已连接的套接字释放资源异常!" + e.getMessage());
        }
    }

    private OnBluetoothConnectListener onBluetoothConnectListener;

    public void setOnBluetoothConnectListener(OnBluetoothConnectListener onBluetoothConnectListener) {
        this.onBluetoothConnectListener = onBluetoothConnectListener;
    }

    //连接状态监听者
    public interface OnBluetoothConnectListener{
        BluetoothDevice onStartConn();  //开始连接
        void onConnSuccess(BluetoothSocket bluetoothSocket);  //连接成功
        void onConnFailure(String errorMsg);  //连接失败
    }






































    /**
     * 读取socket上InputStream输入流数据
     */
    protected void readData() throws IOException {

        Log.w("read()", "开始读取socket上InputStream");
        byte[] dataBuf = new byte[dataLeng];
        int recTotalCount = 0;

        try {
            if (inputStream == null) {
                destory();
                return;
            }

            int count = 0;
            while (true) {
                count = inputStream.available();
                Log.i("count", String.valueOf(count));
                Log.i("inputStream.available()",
                        String.valueOf(inputStream.available()));
                if (count > 0) {
                    int readCount = inputStream.read(dataBuf, recTotalCount,
                            count);
                    recTotalCount += readCount;

                    if (readCount == dataLeng) {
                        break;
                    }
                } else {
                    Log.i("Thread.sleep(100);", "线程阻塞");
                    Thread.sleep(100);
                }
            }

            // 解析到高压、低压、心率数据
            String strTemp = this.parseData(dataBuf);

            String highBloodMeasure = Integer.valueOf(strTemp.substring(4, 8),
                    16).toString();
            String lowBloodMeasure = Integer.valueOf(strTemp.substring(8, 10),
                    16).toString();
            String pulseRate = Integer.valueOf(strTemp.substring(10, 12), 16)
                    .toString();
            Log.i("测量到的高压数据", highBloodMeasure);
            Log.i("测量到的低压数据", lowBloodMeasure);
            Log.i("测量到的心率", pulseRate);
            measureResult.put("highBloodMeasure", highBloodMeasure);
            measureResult.put("lowBloodMeasure", lowBloodMeasure);
            measureResult.put("pulseRate", pulseRate);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("蓝牙数据传送异常!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IOException("未知异常，建议重启程序!");
        } finally {
            destory();
        }

    }

    /**
     * 解析byte[]中的字节流到字符串cs中
     *
     * @param bs
     * @return
     */
    private String parseData(byte[] bs) {
        Log.i("parseData()", "---");
        char[] cs = new char[bs.length];
        for (int i = 0; i < bs.length; i++) {
            cs[i] = (char) bs[i];
        }
        return new String(cs);

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