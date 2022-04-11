package com.example.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> tenbaithi;

    public HistoryAdapter() {
    }

    public HistoryAdapter(Context context, int layout, ArrayList<String> tenbaithi) {
        this.context = context;
        this.layout = layout;
        this.tenbaithi = tenbaithi;
    }

    @Override
    public int getCount() {
        return tenbaithi.size();
    }

    @Override
    public Object getItem(int position) {
        return tenbaithi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);
        TextView tvtenmon=convertView.findViewById(R.id.tvlinehistenmon);
        TextView tvheso=convertView.findViewById(R.id.tvlinehisheso);
        TextView tvtgian=convertView.findViewById(R.id.tvlinehistgian);

        String tenmon=tenbaithi.get(position);
        String arrtenbaithi[]=tenmon.split(" ",4);
        tvtenmon.setText("Bài thi môn: "+arrtenbaithi[3]);
        tvheso.setText("Hệ số: "+arrtenbaithi[1]);
        tvtgian.setText("Thời gian: "+arrtenbaithi[2]);

        return convertView;
    }
    /*void gettenmon(ArrayList<String> tenmon){
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("QLDiemthi");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()){
                    arrtenmon.add(snap.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}
