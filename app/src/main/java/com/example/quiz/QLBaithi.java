package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QLBaithi {
    public String Monhoc;
    public String Dokho;
    public String Cauhoi;
    public String da1;
    public String da2;
    public String da3;
    public String da4;
    public String dad;
    public String dac;

    public QLBaithi() {
    }
    public QLBaithi(QLCauhoi cauhoi){
        this.Monhoc=cauhoi.Monhoc;
        this.Dokho = cauhoi.Dokho;
        this.Cauhoi=cauhoi.Cauhoi;
        this.da1=cauhoi.da1;
        this.da2=cauhoi.da2;
        this.da3=cauhoi.da3;
        this.da4=cauhoi.da4;
        this.dad=cauhoi.dad;
        this.dac="";

    }

    public QLBaithi(String cauhoi, String da1, String da2, String da3, String da4, String dad, String dac) {
        Cauhoi = cauhoi;
        this.da1 = da1;
        this.da2 = da2;
        this.da3 = da3;
        this.da4 = da4;
        this.dad = dad;
        this.dac = dac;
    }


}
