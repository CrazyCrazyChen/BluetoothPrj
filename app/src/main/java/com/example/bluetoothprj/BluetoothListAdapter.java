package com.example.bluetoothprj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class BluetoothListAdapter extends BaseAdapter {
    public List<BluetoothBean> getList() {
        return list;
    }

    public void setList(List<BluetoothBean> list) {
        this.list = list;
    }

    List<BluetoothBean> list;
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

        BluetoothBean app = list.get(position);
        holder.name.setText("NAME："+app.name);
        holder.mac.setText("MAC:"+app.mac);
        holder.statu.setText("状态:"+app.statu);





        final  int pos = position;
       // final  String packageName = app.packageName;

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // itemButton .btnOnClick(pos,"ttst");


            }
        });




        return convertView;
    }



    IItemButton itemButton;
    public void setUninstall(IItemButton itemButton) {
        this.itemButton = itemButton;
    }


    public class ViewHolder{
        TextView name;
        TextView mac;
        TextView statu;
        Button btn;
    }
}
