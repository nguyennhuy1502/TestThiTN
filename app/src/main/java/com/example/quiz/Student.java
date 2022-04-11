package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student extends AppCompatActivity {
    ImageView btVaothi,btHistory,btInfo,btExit;
    QLThisinh user;
    TextView tvHello;
    String keyTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        anhxa();
        Intent  i=getIntent();
        user=new QLThisinh(i.getStringExtra("id"),i.getStringExtra("name"),i.getStringExtra("lop"),i.getStringExtra("pass"));
        tvHello.setText("Xin chào, "+user.name);
        getKey(user.id);
        btVaothi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Student.this,Student_Thi.class);
                i.putExtra("id",user.id);
                i.putExtra("name",user.name);
                i.putExtra("lop",user.lop);
                i.putExtra("pass",user.pass);
                startActivity(i);
            }
        });
        btHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Student.this,LichSuLamBai.class);
                i.putExtra("id",user.id);
                i.putExtra("name",user.name);
                i.putExtra("lop",user.lop);
                i.putExtra("pass",user.pass);
                startActivity(i);
            }
        });
        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Builder=new AlertDialog.Builder(v.getContext());
                Builder.setMessage("Họ và tên: "+user.name+"\n"+"MSV: "+user.id+"\n"+"Lớp: "+user.lop+"\nPassword: "+user.pass);
                Builder.setCancelable(true);
                Builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                Builder.setNegativeButton("Đổi mật khẩu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                        final View popup=getLayoutInflater().inflate(R.layout.popup_edit_pass_sv,null);
                        Button btedit,btcancel;
                        EditText txtoldpass,txtnewpass;

                        btedit=popup.findViewById(R.id.btChangePass);
                        btcancel=popup.findViewById(R.id.btCancelChangePass);
                        txtoldpass=popup.findViewById(R.id.txtoldPass);
                        txtnewpass=popup.findViewById(R.id.txtnewPass);
                        btedit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (txtoldpass.getText().toString().trim().equals("")||txtnewpass.getText().toString().trim().equals(""))
                                    Toast.makeText(Student.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                                else{
                                    if (txtoldpass.getText().toString().equals(user.pass)==false){
                                        Toast.makeText(Student.this, "Mật khẩu cũ nhập không đúng! Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                                        txtoldpass.setText("");
                                    }else if(txtoldpass.getText().toString().equals(txtnewpass.getText().toString())){
                                        Toast.makeText(Student.this, "Mật khẩu mới phải khác mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                                        txtnewpass.setText("");
                                    }else{
                                        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
                                        QLThisinh ts=new QLThisinh(user.id,user.name,user.lop,txtnewpass.getText().toString());
                                        mRef.child("QLThisinh").child(keyTS).setValue(ts, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                if (error==null) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                                    builder.setCancelable(true);
                                                    builder.setMessage("Đổi mật khẩu thành công! Đăng nhập lại ngay?");
                                                    builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Student.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            finish();
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();
                                                }
                                                else{
                                                    AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                                    builder.setCancelable(true);
                                                    builder.setMessage("Đổi mật khẩu không thành công! Vui lòng thử lại!");

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

                        builder.setView(popup);
                        AlertDialog dialog1=builder.create();
                        dialog1.show();

                    }
                });
                AlertDialog dialog=Builder.create();
                dialog.show();
            }
        });
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Student.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    private void anhxa() {
        btVaothi=findViewById(R.id.imgThi);
        btHistory=findViewById(R.id.imgHistory);
        btInfo=findViewById(R.id.imgInfo);
        btExit=findViewById(R.id.imgDangxuat);
        tvHello=findViewById(R.id.tvhellots);
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