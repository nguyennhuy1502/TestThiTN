package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QLDiemthi {
    public String Idsv;
    public String Namesv;
    public String Diemhs1;
    public String Diemhs2;

    public QLDiemthi() {
    }

    public QLDiemthi(String idsv, String namesv, String diemhs1, String diemhs2) {
        Idsv = idsv;
        Namesv = namesv;
        Diemhs1 = diemhs1;
        Diemhs2 = diemhs2;
    }
    public QLDiemthi(String mon,String ts){
        String str[]=ts.split(" ",2);
        Idsv=str[0];
        Namesv=str[1];
        Diemhs1="";
        Diemhs2="";
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("QLDiemthi");
        mRef.child(mon).child(ts).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                upDiem diem=snapshot.getValue(upDiem.class);
                if(diem.heso==1){
                    if (Diemhs1.equals(""))
                        Diemhs1=Float.toString(diem.diem);
                    else
                        Diemhs1=Diemhs1+" "+Float.toString(diem.diem);
                }
                if(diem.heso==2){
                    if (Diemhs2.equals(""))
                        Diemhs2=Float.toString(diem.diem);
                    else
                        Diemhs2=Diemhs1+" "+Float.toString(diem.diem);
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
}
