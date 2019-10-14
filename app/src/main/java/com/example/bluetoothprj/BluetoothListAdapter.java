package com.example.bluetoothprj;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class BluetoothListAdapter extends BaseAdapter {
    public List<BluetoothDevice> getList() {
        return list;
    }

    public void setList(List<BluetoothDevice> list) {
        this.list = list;
    }

    List<BluetoothDevice> list;
    LayoutInflater inflater;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    Context context;

    public BluetoothListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }




    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mac = (TextView) convertView.findViewById(R.id.tv_macaddr);
            holder.statu = (TextView) convertView.findViewById(R.id.tv_statu);
            holder.btn = (Button) convertView.findViewById(R.id.btncon);
         //   holder.btn = (Button) convertView.findViewById(R.id.btncon);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice app = list.get(position);
        holder.name.setText("NAME："+app.getName());
        holder.mac.setText("MAC:"+app.getAddress());
        holder.statu.setText("状态:"+app.getBondState());





       final int pos = position;
       // final  String packageName = app.packageName;



        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("adb", "onClick:__________________条目按钮监听 ");
                //itemButton .btnOnClick(pos,"ttst");





            }
        });




        return convertView;
    }





    public class ViewHolder{
        TextView name;
        TextView mac;
        TextView statu;
        Button btn;
    }
}
