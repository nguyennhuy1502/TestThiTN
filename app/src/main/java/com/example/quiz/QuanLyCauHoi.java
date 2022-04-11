package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class QuanLyCauHoi extends AppCompatActivity {
    Boolean flagsNull=false;
    ImageView btAddch;
    ListView lstCauhoi;
    ArrayList<QLCauhoi> lstch;
    String mon[]={"Tiếng Anh"};
    String dokho[]={"Dễ","Trung bình","Khó"};
    DatabaseReference mData;
    CauhoiAdapter adapter;
    String Monhoc;
    AppCompatSpinner spDokho;
    String keyCH="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_cau_hoi);
        anhxa();
        Intent intent=getIntent();
        Monhoc=intent.getStringExtra("mon");
        mData=FirebaseDatabase.getInstance().getReference();
        lstch=new ArrayList<QLCauhoi>();
        setSpDokho(spDokho,dokho);
        spDokho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    lstch.clear();
                    getListCauhoi2(lstch, spDokho.getSelectedItem().toString());
                    adapter = new CauhoiAdapter(view.getContext(), R.layout.line_cau_hoi, lstch);
                    lstCauhoi.setAdapter(adapter);
                }catch(Exception e){
                    Toast.makeText(QuanLyCauHoi.this, "Vui lòng thêm câu hỏi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btAddch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Builder=new AlertDialog.Builder(v.getContext());
                final View popupAddch= getLayoutInflater().inflate(R.layout.popup_add_cauhoi, null);
                EditText txtcauhoi,txtda1,txtda2,txtda3,txtda4;
                Button btadd,btcancel;
                Spinner spmon,spdokho;
                RadioGroup rdgchon;
                RadioButton rd1,rd2,rd3,rd4;
                //////
                txtcauhoi=popupAddch.findViewById(R.id.txtAddnd);
                txtda1=popupAddch.findViewById(R.id.txtAddda1);
                txtda2=popupAddch.findViewById(R.id.txtAddda2);
                txtda3=popupAddch.findViewById(R.id.txtAddda3);
                txtda4=popupAddch.findViewById(R.id.txtAddda4);
                btadd=popupAddch.findViewById(R.id.btAddch);
                btcancel=popupAddch.findViewById(R.id.btCancelAddch);
                spmon=popupAddch.findViewById(R.id.spAddmh);
                spdokho=popupAddch.findViewById(R.id.spAdddk);
                rdgchon=popupAddch.findViewById(R.id.rdgchon);
                rd1=popupAddch.findViewById(R.id.rdAddda1);
                rd2=popupAddch.findViewById(R.id.rdAddda2);
                rd3=popupAddch.findViewById(R.id.rdAddda3);
                rd4=popupAddch.findViewById(R.id.rdAddda4);


                //////
                SetSpinner(spmon,spdokho,mon,dokho);
                Builder.setView(popupAddch);
                AlertDialog dialog=Builder.create();
                dialog.show();
                ArrayList<String> arrch=new ArrayList<String>();
                getListCauhoi(arrch,spmon,spdokho);
                btcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (txtcauhoi.getText().toString().equals("")||txtda1.getText().toString().equals("")||txtda2.getText().toString().equals("")||txtda3.getText().toString().equals("")||txtda4.getText().toString().equals("")){
                            Toast.makeText(QuanLyCauHoi.this, "Vui lòng nhập đủ thông tin câu hỏi", Toast.LENGTH_SHORT).show();
                        }else{
                            if(KtraTrungda(txtda1,txtda2,txtda3,txtda4)){
                                Toast.makeText(QuanLyCauHoi.this, "Các đáp án không được trùng nhau!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(rdgchon.getCheckedRadioButtonId()==-1){
                                    Toast.makeText(QuanLyCauHoi.this, "Vui lòng chọn 1 đáp án đúng!", Toast.LENGTH_SHORT).show();

                                }else{
                                    String dad="";
                                    if(rd1.isChecked())
                                        dad=txtda1.getText().toString();
                                    if (rd2.isChecked())
                                        dad=txtda2.getText().toString();
                                    if (rd3.isChecked())
                                        dad=txtda3.getText().toString();
                                    if (rd4.isChecked())
                                        dad=txtda4.getText().toString();
                                    mData= FirebaseDatabase.getInstance().getReference();
                                    QLCauhoi cauhoi=new QLCauhoi(spmon.getSelectedItem().toString(),spdokho.getSelectedItem().toString(),txtcauhoi.getText().toString(),txtda1.getText().toString(),txtda2.getText().toString(),txtda3.getText().toString(),txtda4.getText().toString(),dad);

                                    if (KtraAddch(arrch,cauhoi.Cauhoi)){
                                        mData.child("QLCauhoi").child(spmon.getSelectedItem().toString()).child(spdokho.getSelectedItem().toString()).push().setValue(cauhoi, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                if(error==null){
                                                    Toast.makeText(QuanLyCauHoi.this, "Thêm câu hỏi thành công", Toast.LENGTH_SHORT).show();
                                                    txtcauhoi.setText("");
                                                    txtda1.setText("");
                                                    txtda2.setText("");
                                                    txtda3.setText("");
                                                    txtda4.setText("");
                                                    rd1.setChecked(false);
                                                    rd2.setChecked(false);
                                                    rd3.setChecked(false);
                                                    rd4.setChecked(false);
                                                }
                                                else
                                                    Toast.makeText(QuanLyCauHoi.this, "Không thể thêm vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }else{
                                        Toast.makeText(QuanLyCauHoi.this, "Câu hỏi đã tồn tại! Vui lòng nhập câu hỏi khác!", Toast.LENGTH_SHORT).show();
                                        txtcauhoi.setText("");
                                        txtda1.setText("");
                                        txtda2.setText("");
                                        txtda3.setText("");
                                        txtda4.setText("");
                                    }

                                }
                            }


                        }
                    }
                });
            }
        });
        lstCauhoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder Builder=new AlertDialog.Builder(view.getContext());
                final View popupttch=getLayoutInflater().inflate(R.layout.popup_tt_cauhoi,null);
                /////
                TextView ch,da1,da2,da3,da4,dad;
                Button btsua,btxoa;
                ch=popupttch.findViewById(R.id.tvndch);
                da1=popupttch.findViewById(R.id.tvda1);
                da2=popupttch.findViewById(R.id.tvda2);
                da3=popupttch.findViewById(R.id.tvda3);
                da4=popupttch.findViewById(R.id.tvda4);
                dad=popupttch.findViewById(R.id.tvdad);
                btsua=popupttch.findViewById(R.id.btedit);
                btxoa=popupttch.findViewById(R.id.btdel);
                //////////
                QLCauhoi cauhoi=lstch.get(position);
                ch.setText("Câu hỏi: "+cauhoi.Cauhoi);
                da1.setText("Đáp án 1: "+cauhoi.da1);
                da2.setText("Đáp án 2: "+cauhoi.da2);
                da3.setText("Đáp án 3: "+cauhoi.da3);
                da4.setText("Đáp án 4: "+cauhoi.da4);
                dad.setText("Đáp án đúng: "+cauhoi.dad);

                btsua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                        final View popupeditch=getLayoutInflater().inflate(R.layout.popup_edit_cauhoi,null);
                        ///
                        EditText txtcauhoi,txtda1,txtda2,txtda3,txtda4;
                        Button btedit,btcancel;
                        AppCompatSpinner spdokho;
                        RadioGroup rdgchon;
                        RadioButton rd1,rd2,rd3,rd4;
                        //////
                        txtcauhoi=popupeditch.findViewById(R.id.txtAddnd);
                        txtda1=popupeditch.findViewById(R.id.txtAddda1);
                        txtda2=popupeditch.findViewById(R.id.txtAddda2);
                        txtda3=popupeditch.findViewById(R.id.txtAddda3);
                        txtda4=popupeditch.findViewById(R.id.txtAddda4);
                        btedit=popupeditch.findViewById(R.id.btAddch);
                        btcancel=popupeditch.findViewById(R.id.btCancelAddch);
                        spdokho=popupeditch.findViewById(R.id.spAdddk);
                        rdgchon=popupeditch.findViewById(R.id.rdgchon);
                        rd1=popupeditch.findViewById(R.id.rdAddda1);
                        rd2=popupeditch.findViewById(R.id.rdAddda2);
                        rd3=popupeditch.findViewById(R.id.rdAddda3);
                        rd4=popupeditch.findViewById(R.id.rdAddda4);
                        ////
                        txtcauhoi.setText(cauhoi.Cauhoi);
                        txtda1.setText(cauhoi.da1);
                        txtda2.setText(cauhoi.da2);
                        txtda3.setText(cauhoi.da3);
                        txtda4.setText(cauhoi.da4);
                        getKey(cauhoi.Cauhoi,cauhoi.Monhoc,cauhoi.Dokho);
                        setSpDokho(spdokho,dokho);
                        btedit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String edtdokho=spdokho.getSelectedItem().toString();
                                ArrayList<String> arrch=new ArrayList<String>();
                                getListCauhoi3(arrch,cauhoi.Monhoc,edtdokho);
                                if (txtcauhoi.getText().toString().equals("")||txtda1.getText().toString().equals("")||txtda2.getText().toString().equals("")||txtda3.getText().toString().equals("")||txtda4.getText().toString().equals("")){
                                    Toast.makeText(QuanLyCauHoi.this, "Vui lòng nhập đủ thông tin câu hỏi", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(KtraTrungda(txtda1,txtda2,txtda3,txtda4)){
                                        Toast.makeText(QuanLyCauHoi.this, "Các đáp án không được trùng nhau!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        if(rdgchon.getCheckedRadioButtonId()==-1){
                                            Toast.makeText(QuanLyCauHoi.this, "Vui lòng chọn 1 đáp án đúng!", Toast.LENGTH_SHORT).show();

                                        }else{
                                            String dad="";
                                            if(rd1.isChecked())
                                                dad=txtda1.getText().toString();
                                            if (rd2.isChecked())
                                                dad=txtda2.getText().toString();
                                            if (rd3.isChecked())
                                                dad=txtda3.getText().toString();
                                            if (rd4.isChecked())
                                                dad=txtda4.getText().toString();
                                            mData= FirebaseDatabase.getInstance().getReference();
                                            QLCauhoi edtcauhoi=new QLCauhoi(cauhoi.Monhoc,spdokho.getSelectedItem().toString(),txtcauhoi.getText().toString(),txtda1.getText().toString(),txtda2.getText().toString(),txtda3.getText().toString(),txtda4.getText().toString(),dad);
                                            if (cauhoi.equals(edtcauhoi))
                                                Toast.makeText(QuanLyCauHoi.this, "Vui lòng chỉnh sửa câu hỏi!", Toast.LENGTH_SHORT).show();
                                            else{
                                                if (cauhoi.Dokho.equals(edtcauhoi.Dokho)){
                                                    mData.child("QLCauhoi").child(cauhoi.Monhoc).child(spdokho.getSelectedItem().toString()).child(keyCH).setValue(edtcauhoi, new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                            if(error==null){
                                                                Toast.makeText(QuanLyCauHoi.this, "Sửa câu hỏi thành công", Toast.LENGTH_SHORT).show();
                                                                txtcauhoi.setText("");
                                                                txtda1.setText("");
                                                                txtda2.setText("");
                                                                txtda3.setText("");
                                                                txtda4.setText("");
                                                                rd1.setChecked(false);
                                                                rd2.setChecked(false);
                                                                rd3.setChecked(false);
                                                                rd4.setChecked(false);
                                                                Intent intent=new Intent(QuanLyCauHoi.this,QuanLyCauHoi.class);
                                                                intent.putExtra("mon",Monhoc);
                                                                startActivity(intent);
                                                                finish();

                                                            }
                                                            else
                                                                Toast.makeText(QuanLyCauHoi.this, "Không thể sửa! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                }else{
                                                    AlertDialog.Builder builder1=new AlertDialog.Builder(v.getContext());
                                                    builder1.setMessage("Bạn đã thay đổi độ khó câu hỏi! Bạn có chắc chắn muốn thay đổi?");
                                                    builder1.setNegativeButton("Tiếp tục", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if (KtraAddch(arrch,edtcauhoi.Cauhoi)){
                                                                mData.child("QLCauhoi").child(cauhoi.Monhoc).child(spdokho.getSelectedItem().toString()).push().setValue(edtcauhoi, new DatabaseReference.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                                        if(error==null){
                                                                            Toast.makeText(QuanLyCauHoi.this, "Sửa câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                                                                            txtcauhoi.setText("");
                                                                            txtda1.setText("");
                                                                            txtda2.setText("");
                                                                            txtda3.setText("");
                                                                            txtda4.setText("");
                                                                            rd1.setChecked(false);
                                                                            rd2.setChecked(false);
                                                                            rd3.setChecked(false);
                                                                            rd4.setChecked(false);
                                                                            mData.child("QLCauhoi").child(cauhoi.Monhoc).child(cauhoi.Dokho).child(keyCH).removeValue();
                                                                            Intent intent=new Intent(QuanLyCauHoi.this,QuanLyCauHoi.class);
                                                                            intent.putExtra("mon",Monhoc);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                        else
                                                                            Toast.makeText(QuanLyCauHoi.this, "Không thể sửa! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                });

                                                            }else{
                                                                Toast.makeText(QuanLyCauHoi.this, "Câu hỏi đã tồn tại! Vui lòng nhập câu hỏi khác!", Toast.LENGTH_SHORT).show();
                                                                txtcauhoi.setText("");
                                                                txtda1.setText("");
                                                                txtda2.setText("");
                                                                txtda3.setText("");
                                                                txtda4.setText("");
                                                            }
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    builder1.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    AlertDialog dialog=builder1.create();
                                                    dialog.show();

                                                }
                                            }


                                        }
                                    }


                                }

                            }
                        });

                        builder.setView(popupeditch);
                        AlertDialog dialog=builder.create();
                        dialog.show();
                        btcancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                    }
                });
                btxoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                        builder.setCancelable(true);
                        builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mData.child("QLCauhoi").child(cauhoi.Monhoc).child(cauhoi.Dokho).child(keyCH).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error==null){
                                    Toast.makeText(QuanLyCauHoi.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(QuanLyCauHoi.this,QuanLyCauHoi.class);
                                    intent.putExtra("mon",Monhoc);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(QuanLyCauHoi.this, "Không thể xóa! Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                            }
                        });
                            }
                        });
                        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog=builder.create();
                        dialog.show();

                    }
                });
                Builder.setView(popupttch);
                AlertDialog dialog=Builder.create();
                dialog.show();

            }
        });
    }

    private void anhxa() {
    btAddch=findViewById(R.id.imgAddch);
    lstCauhoi=findViewById(R.id.lstCauhoi);
    spDokho=findViewById(R.id.spdokho);
    }
    void SetSpinner(Spinner sp1, Spinner sp2, String arr1[],String arr2[]){
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arr1);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arr2);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp1.setAdapter(adapter1);
        sp2.setAdapter(adapter2);
    }
    Boolean KtraAddch(ArrayList<String> lstch,String ch){
        /*for(String cauhoi:lstch)
            if(cauhoi.equals(ch))
                return false;
            return true;*/
        return true;
    }
    void getListCauhoi(ArrayList<String> lstcauhoi, Spinner spmon,Spinner spdokho){
        DatabaseReference mData=FirebaseDatabase.getInstance().getReference();
        mData.child("QLCauhoi").child(spmon.getSelectedItem().toString()).child(spdokho.getSelectedItem().toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                lstcauhoi.add(snapshot.getValue(QLCauhoi.class).Cauhoi);
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
    void getListCauhoi2(ArrayList<QLCauhoi> lstcauhoi, String dokho){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("QLCauhoi").child(Monhoc).child(dokho).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLCauhoi cauhoi=snapshot.getValue(QLCauhoi.class);
                lstcauhoi.add(cauhoi);
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
    Boolean KtraTrungda(EditText da1, TextView da2,TextView da3,TextView da4){
        /*if (da1.getText().toString().trim().equals(da2.getText().toString().trim()))
            return true;
        if (da1.getText().toString().trim().equals(da3.getText().toString().trim()))
            return true;
        if (da1.getText().toString().trim().equals(da4.getText().toString().trim()))
            return true;
        if (da2.getText().toString().trim().equals(da3.getText().toString().trim()))
            return true;
        if (da2.getText().toString().trim().equals(da4.getText().toString().trim()))
            return true;
        if (da3.getText().toString().trim().equals(da4.getText().toString().trim()))
            return true;*/
        return false;
    }
    void setSpDokho(AppCompatSpinner sp,String arr[]){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp.setAdapter(adapter);
    }
    void getKey(String CH,String mon,String dkho){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("QLCauhoi").child(mon).child(dkho);
        mRef.orderByChild("Cauhoi").equalTo(CH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    keyCH=snap.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getListCauhoi3(ArrayList<String> lstcauhoi, String mon,String dkho){
        DatabaseReference mData=FirebaseDatabase.getInstance().getReference();
        mData.child("QLCauhoi").child(mon).child(dkho).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                lstcauhoi.add(snapshot.getValue(QLCauhoi.class).Cauhoi);
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