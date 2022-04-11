package com.example.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThisinhAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<QLThisinh> ThisinhList;
    String keyTS;

    public ThisinhAdapter() {
    }

    public ThisinhAdapter(Context context, int layout, ArrayList<QLThisinh> thisinhList) {
        this.context = context;
        this.layout = layout;
        ThisinhList = thisinhList;
    }

    @Override
    public int getCount() {
        return ThisinhList.size();
    }

    @Override
    public Object getItem(int position) {
        return ThisinhList.get(position);
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
        TextView txtHoten=convertView.findViewById(R.id.txtName);
        TextView txtMSV=convertView.findViewById(R.id.txtMSV);
        TextView txtLop=convertView.findViewById(R.id.txtLop);
        TextView txtMatkhau=convertView.findViewById(R.id.txtMK);
        ImageView btdelete=convertView.findViewById(R.id.imgEdit);

        //gan gia tri
        QLThisinh thisinh=ThisinhList.get(position);
        txtHoten.setText(thisinh.name);
        txtLop.setText(thisinh.lop);
        txtMSV.setText(thisinh.id);
        txtMatkhau.setText(thisinh.pass);
        //
        getKey(thisinh.id);
        btdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
                mRef.child("QLThisinh").child(keyTS).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error==null){
                            Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Không thể xóa sinh viên này!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return convertView;
    }
    void getKey(String msv){
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("QLThisinh");
        mRef.orderByChild("id").equalTo(msv).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    keyTS=snap.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
