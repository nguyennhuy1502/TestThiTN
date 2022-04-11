package com.example.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryPopupLineAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<QLBaithi> arrBaithi;

    public HistoryPopupLineAdapter() {
    }

    public HistoryPopupLineAdapter(Context context, int layout, ArrayList<QLBaithi> arrBaithi) {
        this.context = context;
        this.layout = layout;
        this.arrBaithi = arrBaithi;
    }

    @Override
    public int getCount() {
        return arrBaithi.size();
    }

    @Override
    public Object getItem(int position) {
        return arrBaithi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);
        TextView tvndch,tvdac;
        RadioButton rda,rdb,rdc,rdd;
        ImageView imgStt;

        tvndch=convertView.findViewById(R.id.tvndch);
        tvdac=convertView.findViewById(R.id.tvdac);
        rda=convertView.findViewById(R.id.rda);
        rdb=convertView.findViewById(R.id.rdb);
        rdc=convertView.findViewById(R.id.rdc);
        rdd=convertView.findViewById(R.id.rdd);
        imgStt=convertView.findViewById(R.id.imgStt);

        QLBaithi cau=arrBaithi.get(position);
        tvndch.setText("Câu "+(position+1)+": "+cau.Cauhoi);
        rda.setText(cau.da1);
        rdb.setText(cau.da2);
        rdc.setText(cau.da3);
        rdd.setText(cau.da4);
        tvdac.setText("Bạn chọn: "+cau.dac);
        if (cau.dac.trim().equals(cau.dad.trim())){
            imgStt.setImageResource(R.drawable.dung);
        }
        else{
            imgStt.setImageResource(R.drawable.sai);
        }
        if (cau.da1.equals(cau.dac))
            rda.setChecked(true);
        else if (cau.da2.equals(cau.dac))
            rdb.setChecked(true);
        else if (cau.da3.equals(cau.dac))
            rdc.setChecked(true);
        else
            rdd.setChecked(true);


        return convertView;
    }
}
