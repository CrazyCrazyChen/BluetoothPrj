package com.example.bluetoothprj;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothReciever extends BroadcastReceiver
{

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub
        Log.w("onReceive()", "发现设备...");
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.i("设备名称", device.getName());
            Log.i("设备地址", device.getAddress());
        }
    }

}