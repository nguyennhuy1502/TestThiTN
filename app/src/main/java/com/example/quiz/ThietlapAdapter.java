package com.example.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ThietlapAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<QLThietlap> lstThietlap;

    public ThietlapAdapter() {
    }

    public ThietlapAdapter(Context context, int layout, ArrayList<QLThietlap> lstThietlap) {
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

        ///
        txtmon=convertView.findViewById(R.id.tvlinemonthi);
        txttgian=convertView.findViewById(R.id.tvlinetgian);
        txtheso=convertView.findViewById(R.id.tvlineheso);
        txtsocau=convertView.findViewById(R.id.tvlinesocau);
        btdelete=convertView.findViewById(R.id.imglinedeletetl);

        ////
        QLThietlap setup=lstThietlap.get(position);
        txtmon.setText("Bài thi: "+setup.Monthi);
        txttgian.setText("Thời gian: "+setup.Thoigian+" phút");
        txtheso.setText("Hệ số: "+setup.Heso);
        txtsocau.setText("Số câu hỏi: "+setup.Socauhoi);
        btdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Builder=new AlertDialog.Builder(v.getContext());
                Builder.setMessage("Bạn có thực sự muốn xóa");
                Builder.setCancelable(true);
                Builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
                        mRef.child("QLThietlap").child(setup.Mathietlap).removeValue();
                        dialog.cancel();
                    }
                });
                Builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=Builder.create();
                dialog.show();
            }
        });


        return convertView;
    }
}
