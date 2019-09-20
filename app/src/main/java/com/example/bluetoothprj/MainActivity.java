package com.example.bluetoothprj;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    public static String TAG = "MainActivity";
    BluetoothAdapter mBluetoothAdapter=null;

    public static final int REQUEST_ENABLE_BT = 1;
    BluetoothDeviceAdapter bluetoothDeviceAdapter;



    protected UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");



    TextView title_tv;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        title_tv = findViewById(R.id.tvtest);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "支持蓝牙！", Toast.LENGTH_SHORT).show();
        }

        RecordThread rec = new RecordThread();
        rec.start();


        Switch ctrlBluetoothSwitch = findViewById(R.id.ctrlBluetoothSwitch);

        ctrlBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Todo

                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    }


                } else {
                    //Todo

                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }


                }
            }
        });







        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);




        search= findViewById(R.id.searchBluetooth);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mBluetoothAdapter.startDiscovery();
             //   mBluetoothAdapter.registerAndDiscover();

                bluetoothDeviceAdapter = new BluetoothDeviceAdapter(MainActivity.this,handler);
                bluetoothDeviceAdapter.openBluetoothAdapter();



              //  Thread thread1 = new Thread(bluetoothDeviceAdapter);
             //   thread1.start();



            }
        });
    }






    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        BluetoothDevice device;

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                 device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                    Log.v(TAG, "find device:" + device.getName()
                            + device.getAddress());
                }
            }
            //搜索完成
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                setTitle("搜索完成");
//                if (bluetoothDeviceAdapter.getCount() == 0) {
//                    Log.v("ok", "find over");
//                }


                BluetoothSocket clienSocket= null;
                try {
                    clienSocket = device. createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    clienSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, "收到啦", Toast.LENGTH_LONG).show();
        }

    };






}

