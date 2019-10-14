package com.example.bluetoothprj;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.provider.ContactsContract.Intents.Insert.NAME;
import static android.provider.Settings.Global.DEVICE_NAME;

public class MainActivity extends AppCompatActivity implements IItemButton ,AdapterView.OnItemClickListener{


    public static String TAG = "MainActivity";

    BluetoothAdapter bluetoothAdapter;
    BluetoothReciever bluetoothReciever;
    ArrayList<BluetoothDevice> list;

    BluetoothListAdapter bluetoothListAdapter;
    BluetoothDeviceAdapter bluetoothDeviceAdapter;


    TextView title_tv;
    Button search;
    ListView listView;


    BluetoothHeadset bluetoothHeadset ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();

        bluetoothListAdapter = new BluetoothListAdapter(getApplicationContext());



        RecordThread.context = getApplicationContext();

        title_tv = findViewById(R.id.tvtest);
        listView = findViewById(R.id.lv_main);


        bluetoothListAdapter.setList(list);

        listView.setAdapter(bluetoothListAdapter);



        listView.setOnItemClickListener(this);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.d(TAG, "onCreate: _________________________不支持蓝牙");
            finish();
        }

        bluetoothReciever = new BluetoothReciever();


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到设备
        registerReceiver(bluetoothReciever, intentFilter);


        //声音播放
        RecordThread rec = new RecordThread();
        rec.start();


//蓝牙开关处理
        Switch ctrlBluetoothSwitch = findViewById(R.id.ctrlBluetoothSwitch);

        ctrlBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: _________________开启蓝牙");
                    bluetoothAdapter.enable();

                    deviceList();

                } else {
                    Log.d(TAG, "onCheckedChanged: _________________关闭蓝牙");
                    bluetoothAdapter.disable();
                }
            }
        });


        search = findViewById(R.id.searchBluetooth);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                deviceList();
                bluetoothAdapter.startDiscovery();

                Log.d(TAG, "onCreate: ___________________这里是list列表" + list.toString());


            }
        });




    }



    @Override
    public void btnOnClick(String packageName) {


        Log.d(TAG, "btnOnClick: ________________________________接口按钮"+packageName);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(bluetoothReciever);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Log.d(TAG, "onItemClick: ————————————————————————————点击了第"+position);




        bluetoothDeviceAdapter = new BluetoothDeviceAdapter(getApplicationContext(), list.get(position));
        new Thread(bluetoothDeviceAdapter).start();


    }


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


             //   for (int i = 0; i < list.size(); i++) {

                    if (!list.contains(device)) {


                        list.add(device);
                    }


                    Log.d(TAG, "onReceive: 发现了这些设备_________\n" + deviceName + "_________\n" + deviceHardwareAddress + "_________\n  ");

                    bluetoothListAdapter.notifyDataSetChanged();

            //    }
            }


                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.

                    Log.d(TAG, "onReceive: " + "结束扫描_________\n");
                }


                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.

                    Log.d(TAG, "onReceive: " + "开始扫描_________\n");
                }


            }


        }


        void deviceList(){

            list.clear();

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.

                Log.d(TAG, "onClick: ______________获取到已绑定蓝牙设备__________\n");
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address

                    Log.d(TAG, "___\n" + deviceName + "\n" + deviceHardwareAddress + "\n"+device.getUuids().toString()+ "_________\n ");
                    list.add(device);
                }

                bluetoothListAdapter.notifyDataSetChanged();

            }

        }

}

