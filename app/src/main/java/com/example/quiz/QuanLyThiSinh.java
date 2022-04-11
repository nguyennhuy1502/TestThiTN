package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class QuanLyThiSinh extends AppCompatActivity {
    ListView lstThisinh;
    ImageView btAddThisinh,btdownload;
    Button btReturn;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    ArrayList<QLThisinh> arrThisinh;
    DatabaseReference mData;
    Boolean flagsNull=false;
    ThisinhAdapter adapter;
    String keyTS="";
    private File filePath=new File(Environment.getExternalStorageDirectory()+"/DSThisinh.xls");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_thi_sinh);
        anhxa();
        mData= FirebaseDatabase.getInstance().getReference();
        arrThisinh=new ArrayList<QLThisinh>();
        getThisinh(mData,arrThisinh);
        adapter=new ThisinhAdapter(this,R.layout.line_thissinh,arrThisinh);
        lstThisinh.setAdapter(adapter);
        lstThisinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder Builder=new AlertDialog.Builder(QuanLyThiSinh.this);
                final View popupEditts=getLayoutInflater().inflate(R.layout.popup_edit_thisinh,null);
                TextView txtmsv,txtname,txtlop,txtpass;
                Button btedit,btcancel;
                txtmsv=popupEditts.findViewById(R.id.txtAddMSV);
                txtname=popupEditts.findViewById(R.id.txtAddName);
                txtlop=popupEditts.findViewById(R.id.txtAddLop);
                txtpass=popupEditts.findViewById(R.id.txtAddMatkhau);
                btedit=popupEditts.findViewById(R.id.btAddThisinh);
                btcancel=popupEditts.findViewById(R.id.btCancelAddThisinh);
                QLThisinh ts= arrThisinh.get(position);
                txtmsv.setText(ts.id);
                txtname.setText(ts.name);
                txtlop.setText(ts.lop);
                txtpass.setText(ts.pass);
                Builder.setView(popupEditts);
                AlertDialog dlog=Builder.create();
                dlog.show();
                getKey(ts.id);
                btedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (txtpass.getText().toString().trim().equals("")||txtlop.getText().toString().trim().equals(""))
                            Toast.makeText(QuanLyThiSinh.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                        else
                        {
                            if (txtpass.getText().toString().equals(ts.pass)==false||txtlop.getText().toString().equals(ts.lop)==false){
                                DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
                                QLThisinh editts=new QLThisinh(txtmsv.getText().toString(),txtname.getText().toString(),txtlop.getText().toString(),txtpass.getText().toString());
                                mRef.child("QLThisinh").child(keyTS).setValue(editts, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error==null){
                                            AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                            builder.setCancelable(true);
                                            builder.setMessage("Đổi thông tin thành công!");
                                            /*builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent=new Intent(QuanLyThiSinh.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    finish();
                                                    dialog.cancel();
                                                }
                                            });*/
                                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            AlertDialog dialog=builder.create();
                                            dialog.show();
                                            
                                        }else{
                                            AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                            builder.setCancelable(true);
                                            builder.setMessage("Đổi thông tin không thành công! Vui lòng thử lại!");

                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            AlertDialog dialog=builder.create();
                                            dialog.show();
                                        }
                                    }
                                });
                            }
                        }

                    }
                });
                btcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlog.cancel();
                    }
                });
            }
        });
        mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("QLThisinh")){
                }
                else
                    flagsNull=true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btAddThisinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder=new AlertDialog.Builder(v.getContext());
                final View popupAddts=getLayoutInflater().inflate(R.layout.popup_add_thisinh,null);
                EditText txtmsv,txthoten,txtLop,txtPass;
                Button btadd,btcancel;
                txtmsv=popupAddts.findViewById(R.id.txtAddMSV);
                txthoten=popupAddts.findViewById(R.id.txtAddName);
                txtLop=popupAddts.findViewById(R.id.txtAddLop);
                txtPass=popupAddts.findViewById(R.id.txtAddMatkhau);
                btadd=popupAddts.findViewById(R.id.btAddThisinh);
                btcancel=popupAddts.findViewById(R.id.btCancelAddThisinh);
                dialogBuilder.setView(popupAddts);
                dialog=dialogBuilder.create();
                dialog.show();
                btadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (KtraTrong(txtmsv,txthoten,txtLop,txtPass)){
                            Toast.makeText(QuanLyThiSinh.this, "Không được để trống bất kì trường nào!", Toast.LENGTH_SHORT).show();
                        }else{
                            if(flagsNull==true){
                                QLThisinh ts=new QLThisinh(txtmsv.getText().toString(),txthoten.getText().toString(),txtLop.getText().toString(),txtPass.getText().toString());
                                mData.child("QLThisinh").push().setValue(ts, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error==null){
                                            Toast.makeText(QuanLyThiSinh.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                            flagsNull=false;
                                            txtmsv.setText("");
                                            txthoten.setText("");
                                            txtLop.setText("");
                                            txtPass.setText("");
                                        }
                                        else{
                                            Toast.makeText(QuanLyThiSinh.this, "Không thể thêm! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                            txtmsv.setText("");
                                            txthoten.setText("");
                                            txtLop.setText("");
                                            txtPass.setText("");
                                        }
                                    }
                                });
                            }else{
                                if(KtraAdd(arrThisinh,txtmsv)){
                                    QLThisinh ts=new QLThisinh(txtmsv.getText().toString(),txthoten.getText().toString(),txtLop.getText().toString(),txtPass.getText().toString());
                                    mData.child("QLThisinh").push().setValue(ts, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error==null){
                                                Toast.makeText(QuanLyThiSinh.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                                flagsNull=false;
                                                txtmsv.setText("");
                                                txthoten.setText("");
                                                txtLop.setText("");
                                                txtPass.setText("");
                                            }
                                            else
                                                Toast.makeText(QuanLyThiSinh.this, "Không thể thêm! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(QuanLyThiSinh.this, "Thí sinh này đã tồn tại!", Toast.LENGTH_SHORT).show();
                                    txtmsv.setText("");
                                    txthoten.setText("");
                                    txtLop.setText("");
                                    txtPass.setText("");
                                }


                            }
                        }
                    }
                });
                btcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });
        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuanLyThiSinh.this,Admin.class);
                startActivity(intent);
                finish();
            }
        });
        btdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tao workbook
                Workbook workbook=new HSSFWorkbook();
                //Tao 1 cell null
                Cell cell=null;

                //tao style cho cell tieu de
                Boolean success=false;
                //Tao bang moi
                Sheet sheet=null;
                sheet=workbook.createSheet("DS Điểm thi");

                //Them hang moi
                Row row=sheet.createRow(0);

                //Add gia trij vao cac cell cua row
                cell=row.createCell(0);
                cell.setCellValue("Mã sinh viên");

                cell=row.createCell(1);
                cell.setCellValue("Tên sinh viên");

                cell=row.createCell(2);
                cell.setCellValue("Lớp");

                cell=row.createCell(3);
                cell.setCellValue("Mật khẩu");

                for (int i=0;i<arrThisinh.size();i++){

                    QLThisinh thisinh=arrThisinh.get(i);
                    row=sheet.createRow(i+1);
                    cell=row.createCell(0);
                    cell.setCellValue(thisinh.id);
                    cell=row.createCell(1);
                    cell.setCellValue(thisinh.name);
                    cell=row.createCell(2);
                    cell.setCellValue(thisinh.lop);
                    cell=row.createCell(3);
                    cell.setCellValue(thisinh.pass);
                }
                int i=0;
                try {
                    while (filePath.exists()){
                        filePath=new File(Environment.getExternalStorageDirectory()+"/DSThisinh("+i+").xls");
                        i++;
                    }

                    FileOutputStream fileOutputStream=new FileOutputStream(filePath);
                    workbook.write(fileOutputStream);
                    success=true;
                    if (fileOutputStream!=null){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (success==true)
                    if(i==0)
                        Toast.makeText(QuanLyThiSinh.this, "Download thành công!/Thisinh.xls", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(QuanLyThiSinh.this, "Download thành công!/DSThisinh("+(i-1)+").xls", Toast.LENGTH_LONG).show();





    }
        });
    }

    private void anhxa() {
    btAddThisinh=findViewById(R.id.imgAddts);
    btdownload=findViewById(R.id.imgDownloadListts);
    lstThisinh=findViewById(R.id.lstThisinh);
    btReturn=findViewById(R.id.btReturnqlts);
    }
    Boolean KtraTrong(TextView txtmsv,TextView txtten,TextView txtlop,TextView txtpass){
        if(txtmsv.getText().toString().trim().equals("")||txtten.getText().toString().trim().equals("")||txtlop.getText().toString().trim().equals("")||txtpass.getText().toString().trim().equals(""))
            return true;
        else
            return false;
    }
    Boolean KtraAdd(ArrayList<QLThisinh> arrts,TextView txtmsv){
        for(QLThisinh ts:arrts){
            if(ts.id.equals(txtmsv.getText().toString()))
                return false;
        }
        return true;
    }
    void getThisinh(DatabaseReference mRef,ArrayList<QLThisinh> arrths){
        mRef.child("QLThisinh").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLThisinh ts=snapshot.getValue(QLThisinh.class);
                arrths.add(ts);
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
    void getKey(String msv){
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("QLThisinh");
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