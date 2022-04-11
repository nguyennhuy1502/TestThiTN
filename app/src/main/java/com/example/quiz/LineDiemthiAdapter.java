package com.example.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LineDiemthiAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> arrts;

    public LineDiemthiAdapter() {
    }

    public LineDiemthiAdapter(Context context, int layout, ArrayList<String> arrts) {
        this.context = context;
        this.layout = layout;
        this.arrts = arrts;
    }

    @Override
    public int getCount() {
        return arrts.size();
    }

    @Override
    public Object getItem(int position) {
        return arrts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);

        TextView tvten=convertView.findViewById(R.id.tvten);
        TextView tvmsv=convertView.findViewById(R.id.tvmsv);

        String ts[]=arrts.get(position).split(" ",2);

        tvten.setText("Họ tên: "+ts[1]);
        tvmsv.setText("MSV: "+ts[0]);

        return convertView;
    }
}
