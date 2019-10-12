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

public class MainActivity extends AppCompatActivity implements IItemButton {


    public static String TAG = "MainActivity";
    BluetoothDeviceAdapter bluetoothDeviceAdapter;




    TextView title_tv;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        title_tv = findViewById(R.id.tvtest);


        BluetoothDeviceAdapter.bluetoothReceiver = new BluetoothReciever();

        bluetoothDeviceAdapter = new BluetoothDeviceAdapter(this,new Handler());




//
//        BluetoothReciever bluetoothReciever= new BluetoothReciever();
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描
//        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
//        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到设备
//        registerReceiver(bluetoothReciever,intentFilter);
//        Log.d(TAG, "onCreate: sh设备绑定-----------------成功");


        //声音播放
        RecordThread rec = new RecordThread();
        rec.start();








//蓝牙开关处理
        Switch ctrlBluetoothSwitch = findViewById(R.id.ctrlBluetoothSwitch);

        ctrlBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Todo

                     Log.d(TAG, "onCheckedChanged: guanbisaomiaolanya hanshu");

                }
            }
        });




        search = findViewById(R.id.searchBluetooth);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//

                new Thread(bluetoothDeviceAdapter).start();


            }
        });

    }


    @Override
    public void btnOnClick(int pos, String packageName) {

    }
}


