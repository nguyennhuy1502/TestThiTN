package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseReference mData;
    ArrayList<adminaccount> lstad;
    ArrayList<QLThisinh> lstts;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private static final String TAG = "MyActivity";
    EditText txtUser,txtPass;
    Button btSign,btForget;
    TextView txtHidden;
    QLThisinh ts;
    SwitchCompat swQTV;
    private static String secretcode="addqtv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        lstad=new ArrayList<adminaccount>();
        //Nút thêm admin ẩn
        txtHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialogBuilder=new AlertDialog.Builder(v.getContext());
               final View popupView=getLayoutInflater().inflate(R.layout.popup_add_qtv,null);
               EditText txtuser=popupView.findViewById(R.id.txtUserAdmin);
               EditText txtpass=popupView.findViewById(R.id.txtPassAdmin);
               EditText txtmbm=popupView.findViewById(R.id.txtMabaomat);
               Button btadd=popupView.findViewById(R.id.btDangkiQTV);
               Button btcancel=popupView.findViewById(R.id.btHuyDangkiQTV);

                dialogBuilder.setView(popupView);
                dialog=dialogBuilder.create();
                dialog.show();
                mData=FirebaseDatabase.getInstance().getReference();

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
               btadd.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       if (txtuser.getText().toString().trim().equals("")||txtpass.getText().toString().trim().equals("")||txtmbm.getText().toString().trim().equals("")){
                           Toast.makeText(MainActivity.this, "Nhập đầy đủ thông tin để thêm quản trị viên", Toast.LENGTH_SHORT).show();
                       }
                       else{
                            if(txtmbm.getText().toString().equals(secretcode)==false)
                                Toast.makeText(MainActivity.this, "Sai mã bảo mật, không thể thêm quản trị viên!", Toast.LENGTH_SHORT).show();
                            else{
                                if(XuLyAdd(lstad,txtuser)){
                                    adminaccount newacc=new adminaccount(txtuser.getText().toString(),txtpass.getText().toString());
                                    mData.child("AdminAccounts").push().setValue(newacc, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error == null) {
                                                Toast.makeText(MainActivity.this, "Thêm quản trị viên thành công!", Toast.LENGTH_SHORT).show();
                                                txtuser.setText("");
                                                txtpass.setText("");
                                                txtmbm.setText("");
                                            } else {
                                                Toast.makeText(MainActivity.this, "Không thể thêm quản trị viên! Vui lòng kiểm tra lại!!!!", Toast.LENGTH_SHORT).show();
                                                txtuser.setText("");
                                                txtpass.setText("");
                                                txtmbm.setText("");
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(MainActivity.this, "Admin này đã tồn tại! Vui lòng thêm Tài khoản khác!!!", Toast.LENGTH_SHORT).show();
                                    txtuser.setText("");
                                    txtpass.setText("");
                                    txtmbm.setText("");
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
        mData=FirebaseDatabase.getInstance().getReference();
        lstad=new ArrayList<adminaccount>();
        lstts=new ArrayList<QLThisinh>();
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
        mData.child("QLThisinh").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                QLThisinh ts=snapshot.getValue(QLThisinh.class);
                lstts.add(ts);
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
        btSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swQTV.isChecked()){
                    if(XuLySignQTV(lstad,txtUser,txtPass)){
                        Toast.makeText(MainActivity.this, "Đăng nhập quản trị viên thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(MainActivity.this,Admin.class);
                        startActivity(intent);
                        txtUser.setText("");
                        txtPass.setText("");
                    }else{
                        Toast.makeText(MainActivity.this, "Đăng nhập không thành công! Vui lòng kiểm tra lại username/password!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    if(XuLySignTS(lstts,txtUser,txtPass)){
                        ts=getThisinh(lstts,txtUser);
                        Toast.makeText(MainActivity.this, "Đăng nhập thí sinh thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(MainActivity.this,Student.class);
                        intent.putExtra("id",ts.id);
                        intent.putExtra("name",ts.name);
                        intent.putExtra("lop",ts.lop);
                        intent.putExtra("pass",ts.pass);
                        startActivity(intent);
                        txtUser.setText("");
                        txtPass.setText("");
                    }else{
                        Toast.makeText(MainActivity.this, "Đăng nhập không thành công! Vui lòng kiểm tra lại username/password!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    void anhxa(){
        btForget=findViewById(R.id.btForgot);
        btSign=findViewById(R.id.btSign);
        txtUser=findViewById(R.id.txtUser);
        txtPass=findViewById(R.id.txtPass);
        txtHidden=findViewById(R.id.txtHidden);
        swQTV=findViewById(R.id.swQTV);
    }
    boolean XuLyAdd(ArrayList<adminaccount> lstad,TextView txtuser){
        for(adminaccount ad:lstad) {
            if (ad.username.equals(txtuser.getText().toString()))
                return false;
        }
        return true;
    }
    boolean XuLySignQTV(ArrayList<adminaccount> lstad,TextView user,TextView pass){
        for (adminaccount ad:lstad){
            if(ad.username.equals(user.getText().toString())&&ad.password.equals(pass.getText().toString())){
                return true;
            }
        }
        return false;
    }
    boolean XuLySignTS(ArrayList<QLThisinh> lstts,TextView user,TextView pass){
        for (QLThisinh ts:lstts){
            if(ts.id.equals(user.getText().toString())&&ts.pass.equals(pass.getText().toString())){
                return true;
            }
        }
        return false;
    }
    QLThisinh getThisinh(ArrayList<QLThisinh> lstts,TextView txtUser){
        for(QLThisinh ts:lstts)
            if (ts.id.equals(txtUser.getText().toString().trim()))
                return ts;
            return new QLThisinh();
    }

}