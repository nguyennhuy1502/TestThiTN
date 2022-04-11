package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Student_Thi extends AppCompatActivity {
    QLThisinh user;
    AppCompatSpinner spmon;
    ImageView btReload;
    ListView lstBaithi;
    ArrayList<String> arrtenmon;
    ArrayList<QLThietlap> arrThietlap;
    QLThietlap setup;
    DatabaseReference mRef;
    BaithiAdapter adapter;
    Context mContext;
    ArrayList<QLBaithi> arrBaithi;
    ArrayList<QLCauhoi> arrCauhoide,arrCauhoitb,arrCauhoikho;
    LineThiAdapter lineThiAdapter;
    Boolean flagsVaothi=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_thi);
        anhxa();
        arrCauhoide=new ArrayList<QLCauhoi>();
        arrCauhoitb=new ArrayList<QLCauhoi>();
        arrCauhoikho=new ArrayList<QLCauhoi>();
        arrBaithi=new ArrayList<QLBaithi>();
        arrtenmon=new ArrayList<String>();
        arrThietlap=new ArrayList<QLThietlap>();
        mContext=getApplicationContext();
        Intent i=getIntent();
        user=new QLThisinh(i.getStringExtra("id"),i.getStringExtra("name"),i.getStringExtra("lop"),i.getStringExtra("pass"));
        SetSpinner(spmon,arrtenmon);
        spmon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getThietlap(arrThietlap,spmon.getSelectedItem().toString());
                adapter=new BaithiAdapter(view.getContext(),R.layout.line_setup,arrThietlap);
                lstBaithi.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Student_Thi.this,Student_Thi.class);
                startActivity(intent);
                finish();
            }
        });
        lstBaithi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setup=arrThietlap.get(position);
                getListCauhoi2(arrCauhoide,arrCauhoitb,arrCauhoikho);
                flagsVaothi=true;
                KtraVaoThi(setup);

                    AlertDialog.Builder Builder=new AlertDialog.Builder(view.getContext());
                    Builder.setCancelable(true);
                    Builder.setMessage("Bạn có muốn vào thi?");
                    Builder.setNegativeButton("Vào thi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (flagsVaothi == true) {
                                try{
                                    Tronde(arrCauhoide, arrCauhoitb, arrCauhoikho, arrBaithi);
                                    lineThiAdapter = new LineThiAdapter(view.getContext(), R.layout.line_thi, arrBaithi);
                                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View viewLambai = inflater.inflate(R.layout.popup_lambai, null);
                                    PopupWindow popupLambai = new PopupWindow(viewLambai, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                                    ListView lstbaithi = viewLambai.findViewById(R.id.lstBaithi);
                                    Button btnop = viewLambai.findViewById(R.id.btNopbai);
                                    TextView tvtimer=viewLambai.findViewById(R.id.tvtimerthi);
                                    //
                                    btnop.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("QLDiemthi").child(setup.Monthi).child(user.id + " " + user.name).child(setup.Mathietlap);
                                            mData.setValue(new upDiem(Chamdiem(arrBaithi), setup.Heso), new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    if (error == null) {
                                                        upLoadbaithi(arrBaithi, setup, user);
                                                        Toast.makeText(mContext, "Nộp bài thàn công! " + Chamdiem(arrBaithi) + " điểm", Toast.LENGTH_SHORT).show();
                                                        popupLambai.dismiss();
                                                    } else
                                                        Toast.makeText(mContext, "Lỗi hệ thống! không thể nập bài", Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                        }
                                    });
                                    lstbaithi.setAdapter(lineThiAdapter);
                                    popupLambai.showAtLocation(view, Gravity.CENTER, 0, 0);
                                    CountDownTimer timer=new CountDownTimer(setup.Thoigian*60000,1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            tvtimer.setText(millisUntilFinished/60000+":"+(millisUntilFinished%60000)/1000);
                                        }

                                        @Override
                                        public void onFinish() {
                                            tvtimer.setText("Hết giờ! Nộp bài");
                                            btnop.callOnClick();
                                        }
                                    }.start();
                                    dialog.cancel();
                                }catch (Exception e) {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                                    builder.setCancelable(true);
                                    builder.setMessage("Ngân hàng câu hỏi không đủ! Vui lòng viên hệ giáo viên để thêm câu hỏi");
                                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog dialog1=builder.create();
                                    dialog1.show();
                                }

                            }else
                                Toast.makeText(Student_Thi.this, "Bạn đã làm bài thi này rồi!Vui lòng chọn bài khác", Toast.LENGTH_SHORT).show();

                        }
                    });
                    Builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                        AlertDialog dialog=Builder.create();
                        dialog.show();





            }
        });
    }

    private void anhxa() {
        btReload=findViewById(R.id.imgReloadbaithi);
        lstBaithi=findViewById(R.id.lstBaithi);
        spmon=findViewById(R.id.spThiChonmon);
    }
    void getThietlap(ArrayList<QLThietlap> lsttl,String mon){
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
        mRef.child("QLThietlap").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLThietlap thietlap=snapshot.getValue(QLThietlap.class);
                if(thietlap.Monthi.equals(mon)) {
                    lsttl.add(thietlap);
                    adapter.notifyDataSetChanged();
                }
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
    void KtraVaoThi(QLThietlap thietlap){
        flagsVaothi=true;
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("QLDiemthi").child(thietlap.Monthi).child(user.id+" "+user.name).child(thietlap.Mathietlap).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    flagsVaothi=false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    void getListCauhoi2(ArrayList<QLCauhoi> arrde,ArrayList<QLCauhoi> arrtb,ArrayList<QLCauhoi> arrkho){
        mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("QLCauhoi").child(setup.Monthi).child("Dễ").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLCauhoi cauhoi=snapshot.getValue(QLCauhoi.class);
                arrde.add(cauhoi);
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
        mRef.child("QLCauhoi").child(setup.Monthi).child("Trung bình").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLCauhoi cauhoi=snapshot.getValue(QLCauhoi.class);
                arrtb.add(cauhoi);
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
        mRef.child("QLCauhoi").child(setup.Monthi).child("Khó").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLCauhoi cauhoi=snapshot.getValue(QLCauhoi.class);
                arrkho.add(cauhoi);
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
    void Tronde(ArrayList<QLCauhoi> arrde,ArrayList<QLCauhoi> arrtb,ArrayList<QLCauhoi> arrkho,ArrayList<QLBaithi> arrbaithi){
        Collections.shuffle(arrde);
        Collections.shuffle(arrtb);
        Collections.shuffle(arrkho);
        String arrcautruc[]=setup.Cautruc.split(" ");
        int cautruc[]={Integer.parseInt(arrcautruc[0]),Integer.parseInt(arrcautruc[1]),Integer.parseInt(arrcautruc[2])};
        for (int i=0;i<cautruc[0];i++){
             QLCauhoi cauhoi=arrde.get(i);
             arrbaithi.add(new QLBaithi(cauhoi));
        }
        for (int i=0;i<cautruc[1];i++){
            QLCauhoi cauhoi=arrtb.get(i);
            arrbaithi.add(new QLBaithi(cauhoi));
        }
        for (int i=0;i<cautruc[2];i++){
            QLCauhoi cauhoi=arrkho.get(i);
            arrbaithi.add(new QLBaithi(cauhoi));
        }
    }
    float Chamdiem(ArrayList<QLBaithi> baithi){
        float diem1cau= (float) (10.0/baithi.size());
        float diem=0;

        for(QLBaithi cau:baithi){
            if(cau.dac.trim().equals(cau.dad.trim())){
                diem+=diem1cau;
            }
        }
        return (float)Math.round(diem*100)/100;
    }
    void upLoadbaithi(ArrayList<QLBaithi> arrbaithi,QLThietlap thietlap,QLThisinh user){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("QLDethi").child(thietlap.Monthi).child(user.id).child(setup.Mathietlap+" "+setup.Heso+" "+setup.Thoigian);
        for(QLBaithi cauhoi:arrbaithi)
            mRef.push().setValue(cauhoi);

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


}