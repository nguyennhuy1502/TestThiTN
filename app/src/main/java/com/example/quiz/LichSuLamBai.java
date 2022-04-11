package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class LichSuLamBai extends AppCompatActivity {
    TextView tvHellohis;
    ListView lstHistory;
    AppCompatSpinner spMonhistory;
    ArrayList<String> arrHistoryBaithi;
    QLThisinh user;
    ArrayList<String> arrtenmon;
    HistoryAdapter adapter;
    ArrayList<QLBaithi> arrBaithi;
    ArrayList<String> arrtenbaithi;
    HistoryPopupLineAdapter adapterpopup;
    float diem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_lam_bai);
        anhxa();
        Intent i=getIntent();
        user = new QLThisinh(i.getStringExtra("id"), i.getStringExtra("name"), i.getStringExtra("lop"), i.getStringExtra("pass"));
        arrtenmon=new ArrayList<String>();
        arrHistoryBaithi=new ArrayList<String>();
        arrBaithi=new ArrayList<QLBaithi>();
        arrtenbaithi=new ArrayList<String>();
        tvHellohis.setText("Xin chào, "+user.name);
        SetSpinner(spMonhistory,arrtenmon);

        spMonhistory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getTenbaithi(arrHistoryBaithi,spMonhistory.getSelectedItem().toString());
                adapter=new HistoryAdapter(view.getContext(),R.layout.line_history,arrHistoryBaithi);
                lstHistory.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lstHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrBaithi.clear();
                getBaithi(arrBaithi,spMonhistory.getSelectedItem().toString(),arrtenbaithi.get(position));
                adapterpopup = new HistoryPopupLineAdapter(view.getContext(), R.layout.line_popup_history, arrBaithi);
                AlertDialog.Builder Build=new AlertDialog.Builder(view.getContext());
                Build.setMessage("Xem lại bài thi này?");
                Build.setCancelable(true);
                Build.setNegativeButton("Vào xem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder Builder=new AlertDialog.Builder(view.getContext());
                        final View popuphis= getLayoutInflater().inflate(R.layout.popup_history_baithi,null);
                        TextView tvdiemthi=popuphis.findViewById(R.id.tvdiemthi);
                        ListView lstbaithi=popuphis.findViewById(R.id.lstbaithipopuphis);
                        Button btok=popuphis.findViewById(R.id.btexitpopuphis);
                        Builder.setView(popuphis);
                        AlertDialog dial=Builder.create();
                        dial.show();
                        lstbaithi.setAdapter(adapterpopup);
                        btok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dial.cancel();
                            }
                        });

                        tvdiemthi.setText("Điểm: "+Chamdiem());
                        dialog.cancel();
                    }
                });
                Build.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog=Build.create();
                dialog.show();

            }
        });


    }

    private void anhxa() {
    tvHellohis=findViewById(R.id.tvHellohis);
    spMonhistory=findViewById(R.id.spLichsuMon);
    lstHistory=findViewById(R.id.lstHistory);
    }

    void SetSpinner(AppCompatSpinner spmon,ArrayList<String> arrtenmon){
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrtenmon);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("QLCauhoi").addValueEventListener(new ValueEventListener() {
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
    void getTenbaithi(ArrayList<String> arrtbt,String mon){
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
        mRef.child("QLDethi").child(mon).child(user.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child:snapshot.getChildren()){
                    arrtbt.add(child.getKey()+" "+mon);
                    arrtenbaithi.add(child.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getBaithi(ArrayList<QLBaithi> arrbt,String mon,String tenbai){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("QLDethi").child(mon).child(user.id).child(tenbai).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLBaithi baithi=snapshot.getValue(QLBaithi.class);
                arrbt.add(baithi);
                adapterpopup.notifyDataSetChanged();
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
    float Chamdiem(){
        float diem1cau= (float) (10.0/arrBaithi.size());
        float diem=0;

        for(QLBaithi cau:arrBaithi){
            if(cau.dac.equals(cau.dad)){
                diem=diem+diem1cau;
            }
        }
        return (float)Math.round(diem*100)/100;
    }
}