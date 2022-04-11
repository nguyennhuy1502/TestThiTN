package com.example.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BaithiAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<QLThietlap> lstThietlap;

    public BaithiAdapter() {
    }

    public BaithiAdapter(Context context, int layout, ArrayList<QLThietlap> lstThietlap) {
        this.context = context;
        this.layout = layout;
        this.lstThietlap = lstThietlap;
    }

    @Override
    public int getCount() {
        return lstThietlap.size();
    }

    @Override
    public Object getItem(int position) {
        return lstThietlap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);
        TextView txtmon,txttgian,txtheso,txtsocau;
        ImageView btdelete;

        txtmon=convertView.findViewById(R.id.tvlinemonthi);
        txttgian=convertView.findViewById(R.id.tvlinetgian);
        txtheso=convertView.findViewById(R.id.tvlineheso);
        txtsocau=convertView.findViewById(R.id.tvlinesocau);
        btdelete=convertView.findViewById(R.id.imglinedeletetl);

        QLThietlap setup=lstThietlap.get(position);
        txtmon.setText("Bài thi: "+setup.Monthi);
        txtheso.setText("Hệ số: "+setup.Heso);
        txtsocau.setText("Số câu: "+setup.Socauhoi);
        txttgian.setText("Thời gian "+setup.Thoigian+" phút");
        btdelete.setEnabled(false);
        btdelete.setVisibility(View.INVISIBLE);

        return convertView;
    }
}
