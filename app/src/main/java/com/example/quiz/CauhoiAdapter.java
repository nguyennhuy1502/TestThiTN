package com.example.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CauhoiAdapter  extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<QLCauhoi> ListCauhoi;

    public CauhoiAdapter() {
    }

    public CauhoiAdapter(Context context, int layout, ArrayList<QLCauhoi> listCauhoi) {
        this.context = context;
        this.layout = layout;
        ListCauhoi = listCauhoi;
    }

    @Override
    public int getCount() {
        return ListCauhoi.size();
    }

    @Override
    public Object getItem(int position) {
        return ListCauhoi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);
        //anhxa
        TextView txtNumber,txtNoidung;
        txtNumber=convertView.findViewById(R.id.txtnumch);
        txtNoidung=convertView.findViewById(R.id.txtnoidung);
        //gans giatri
        QLCauhoi cauhoi=ListCauhoi.get(position);
        txtNumber.setText(Integer.toString(position+1));
        txtNoidung.setText(cauhoi.Cauhoi);
        return convertView;
    }
}
