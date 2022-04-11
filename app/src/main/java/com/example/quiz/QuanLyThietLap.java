package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuanLyThietLap extends AppCompatActivity {
    ImageView btAddthietlap,btReload;
    ListView lstThietlap;
    String heso[]={"1","2"};
    ArrayList<String> tenmon;
    DatabaseReference mData;
    ArrayList<QLThietlap> lsttl;
    ThietlapAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_thiet_lap);
        anhxa();
        lsttl=new ArrayList<QLThietlap>();
        getListThietlap(lsttl);
        adapter=new ThietlapAdapter(this,R.layout.line_setup,lsttl);
        lstThietlap.setAdapter(adapter);
        btAddthietlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenmon=new ArrayList<String>();
                mData=FirebaseDatabase.getInstance().getReference();
                AlertDialog.Builder Builder=new AlertDialog.Builder(v.getContext());
                final View popupadtl=getLayoutInflater().inflate(R.layout.popup_add_thietlap,null);

                EditText txttgian,txtsocau,txtcautruc;
                Spinner spmon,spheso;
                Button btadd;
                ImageView alerttgian,alertsocau,alertcautruc;
                ///
                txttgian=popupadtl.findViewById(R.id.txtAddtgianlam);
                txtsocau=popupadtl.findViewById(R.id.txtAddSocauhoi);
                txtcautruc=popupadtl.findViewById(R.id.txtAddcautruc);
                spmon=popupadtl.findViewById(R.id.spAddtlmon);
                spheso=popupadtl.findViewById(R.id.spAddheso);
                btadd=popupadtl.findViewById(R.id.btAddtl);
                alerttgian=popupadtl.findViewById(R.id.imgAlertAddtltgian);
                alertsocau=popupadtl.findViewById(R.id.imgAlertAddtlsocau);
                alertcautruc=popupadtl.findViewById(R.id.imgAlertAddtlcautruc);
                alerttgian.setVisibility(View.INVISIBLE);
                alertsocau.setVisibility(View.INVISIBLE);
                alertcautruc.setVisibility(View.INVISIBLE);
                ////
                SetSpinner(spmon,spheso,tenmon,heso);
                Builder.setView(popupadtl);
                AlertDialog dialog=Builder.create();
                dialog.show();
                btadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alerttgian.setVisibility(View.INVISIBLE);
                        alertsocau.setVisibility(View.INVISIBLE);
                        alertcautruc.setVisibility(View.INVISIBLE);
                        String cautruc[]=txtcautruc.getText().toString().split(" ");
                        int total=0;
                        try {
                            total=Integer.parseInt(cautruc[0])+Integer.parseInt(cautruc[1])+Integer.parseInt(cautruc[2]);
                        }catch (Exception e){
                            Toast.makeText(QuanLyThietLap.this, "Cấu trúc có dạng <số câu dễ><cách><số câu trung bình><cách><số câu khó>", Toast.LENGTH_SHORT).show();
                            alertcautruc.setVisibility(View.VISIBLE);
                        }

                        if (txttgian.getText().toString().equals("")||txtsocau.getText().toString().equals("")||txtcautruc.getText().toString().equals("")){
                            Toast.makeText(QuanLyThietLap.this, "Không để trống các trường này!", Toast.LENGTH_SHORT).show();
                            alerttgian.setVisibility(View.VISIBLE);
                            alertsocau.setVisibility(View.VISIBLE);
                            alertcautruc.setVisibility(View.VISIBLE);
                        }else if(Integer.parseInt(txttgian.getText().toString())>180||Integer.parseInt(txttgian.getText().toString())<10){
                            Toast.makeText(QuanLyThietLap.this, "Vui lòng nhập thời gian trong khoảng 10-180 phút", Toast.LENGTH_SHORT).show();
                            alerttgian.setVisibility(View.VISIBLE);
                            txttgian.setText("");
                        }else if(Integer.parseInt(txtsocau.getText().toString())<10||Integer.parseInt(txtsocau.getText().toString())>120){
                            Toast.makeText(QuanLyThietLap.this, "Vui lòng nhập số câu trong khoảng 10-120 câu!", Toast.LENGTH_SHORT).show();
                            alertsocau.setVisibility(View.VISIBLE);
                            txtsocau.setText("");
                        }else if(total!=Integer.parseInt(txtsocau.getText().toString())){
                            Toast.makeText(QuanLyThietLap.this, "Tổng số câu trong cấu trúc phỉ bằng số câu hỏi ở mục trên!", Toast.LENGTH_SHORT).show();
                            alertcautruc.setVisibility(View.VISIBLE);
                            txtcautruc.setText("");
                        }else{
                            String matl=mData.child("QLThietlap").push().getKey();
                            QLThietlap thietlap=new QLThietlap(matl,spmon.getSelectedItem().toString(),Integer.parseInt(txttgian.getText().toString()),Integer.parseInt(txtsocau.getText().toString()),txtcautruc.getText().toString(),Integer.parseInt(spheso.getSelectedItem().toString()));
                            mData.child("QLThietlap").child(matl).setValue(thietlap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if(error==null) {
                                        Toast.makeText(QuanLyThietLap.this, "Thêm thiết lập thành công!", Toast.LENGTH_SHORT).show();
                                        txttgian.setText("");
                                        txtsocau.setText("");
                                        txtcautruc.setText("");
                                    }
                                    else {
                                        Toast.makeText(QuanLyThietLap.this, "Không thể thêm! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        btReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(QuanLyThietLap.this,QuanLyThietLap.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void anhxa() {
        btAddthietlap=findViewById(R.id.imgAddtl);
        lstThietlap=findViewById(R.id.lstThietlap);
        btReload=findViewById(R.id.imgReloadtl);
    }
    void SetSpinner(Spinner spmon,Spinner spheso,ArrayList<String> lstmh,String heso[]){
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,lstmh);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,heso);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mData.child("QLCauhoi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child:snapshot.getChildren()){
                    String mh=child.getKey();
                    lstmh.add(mh);
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spmon.setAdapter(adapter1);
        spheso.setAdapter(adapter2);
    }
    void getListChildName(ArrayList<String> lsttenmon,DatabaseReference mRef){
        mRef.child("QLCauhoi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child:snapshot.getChildren()){
                    String mh=child.getKey();
                    lsttenmon.add(mh);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getListThietlap(ArrayList<QLThietlap> lstgettl){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("QLThietlap").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLThietlap thietlap=snapshot.getValue(QLThietlap.class);
                lstgettl.add(thietlap);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}