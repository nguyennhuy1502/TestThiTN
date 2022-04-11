package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {
    ImageView btQLThisinh,btQLDethi,btQLDiem,btThietlap,btKtraloi,btExit;
    EditText txtUser,txtPass;
    String dokho[]={"Dễ","Trung bình","Khó"};
    ArrayList<String> arrtenmon;
    ArrayList<String> arrMon;
    TextView txtlistAd;
    ArrayList<adminaccount> lstad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        anhxa();
        ArrayList<adminaccount> lstad=new ArrayList<adminaccount>();
        getAd(lstad);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Admin.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        btQLThisinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,QuanLyThiSinh.class);
                startActivity(intent);
            }
        });
        btQLDethi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrMon=new ArrayList<String>();
                AlertDialog.Builder Builder=new AlertDialog.Builder(v.getContext());
                final View popupchoose=getLayoutInflater().inflate(R.layout.popup_choose_qlch,null);
                Spinner spmon,spdokho;
                Button btnext;
                spmon=popupchoose.findViewById(R.id.spMonhoc);
                btnext=popupchoose.findViewById(R.id.btGo);
                SetSpinner(spmon,arrMon);
                Builder.setView(popupchoose);
                AlertDialog dialog=Builder.create();
                dialog.show();

                btnext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            Intent intent = new Intent(Admin.this, QuanLyCauHoi.class);
                            intent.putExtra("mon", spmon.getSelectedItem().toString());
                            startActivity(intent);
                            dialog.cancel();
                        }catch (Exception e){
                            Toast.makeText(Admin.this, "Chưa có câu hỏi! Vui lòng thêm 1 câu hỏi vào!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Admin.this, QuanLyCauHoi.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        btThietlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this, QuanLyThietLap.class);
                startActivity(intent);
            }
        });
        btQLDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                final View popupaccept=getLayoutInflater().inflate(R.layout.popup_chonmon_qldiem,null);

                AppCompatSpinner spmon=popupaccept.findViewById(R.id.spchonmonqldiem);
                Button btgo=popupaccept.findViewById(R.id.bttoqldiem);
                arrtenmon=new ArrayList<String>();
                SetSpinnerqldiem(spmon,arrtenmon);

                builder.setView(popupaccept);
                AlertDialog dialog=builder.create();
                dialog.show();
                btgo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Admin.this,QuanLyDiemThi.class);
                        intent.putExtra("mon",spmon.getSelectedItem().toString());
                        startActivity(intent);
                    }
                });
            }
        });
        btKtraloi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin.this,QuanLyBaoLoi.class);
                startActivity(intent);
            }
        });
        txtlistAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dsam=String.format("%5s%15s%15s\n","STT","Username","Password");
                for (adminaccount a:lstad) {
                    dsam+=String.format("%5d%15s%15s\n",lstad.indexOf(a),a.username,a.password);
                }
                AlertDialog.Builder builder= new AlertDialog.Builder(v.getContext());
                builder.setMessage(dsam);
                AlertDialog dialog=builder.create();
                dialog.show();


            }
        });
    }

    private void anhxa() {
        btQLThisinh=findViewById(R.id.btqlts);
        btKtraloi=findViewById(R.id.btktraloi);
        btQLDethi=findViewById(R.id.btqlde);
        btQLDiem=findViewById(R.id.btqldiem);
        btExit=findViewById(R.id.btexit);
        btThietlap=findViewById(R.id.btsetup);
        txtUser=findViewById(R.id.txtUser);
        txtPass=findViewById(R.id.txtPass);
        txtlistAd=findViewById(R.id.txtdsad);
    }
    void SetSpinner(Spinner sp1, ArrayList<String> arr1){
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arr1);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("QLCauhoi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child:snapshot.getChildren()){
                    arr1.add(child.getKey());
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sp1.setAdapter(adapter1);
    }
    void SetSpinnerqldiem(AppCompatSpinner spmon,ArrayList<String> arrtenmon){
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrtenmon);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("QLDiemthi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child:snapshot.getChildren()){
                    arrtenmon.add(child.getKey());
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spmon.setAdapter(adapter1);
    }
    void getAd(ArrayList<adminaccount> lstad){
        DatabaseReference mData=FirebaseDatabase.getInstance().getReference();
        mData.child("AdminAccounts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adminaccount ad=snapshot.getValue(adminaccount.class);
                lstad.add(ad);

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