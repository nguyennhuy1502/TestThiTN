package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Sampler;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class QuanLyDiemThi extends AppCompatActivity {
    private ImageView btDownload,btreload;
    private ListView lstDiemthi;
    private ArrayList<String> ts;
    private String mon;
    private LineDiemthiAdapter adapter;
    private ArrayList<QLDiemthi> arrDiemthi;
    private File filePath=new File(Environment.getExternalStorageDirectory()+"/DSDiem.xls");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_diem_thi);
        anhxa();
        Intent intent=getIntent();
        mon=intent.getStringExtra("mon");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ts=new ArrayList<String>();
        arrDiemthi=new ArrayList<QLDiemthi>();
        getThisinh(ts);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Cần đồng bộ dữ liệu điểm thi");
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Đồng bộ ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btreload.callOnClick();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
        adapter=new LineDiemthiAdapter(this,R.layout.line_diemthi,ts);
        lstDiemthi.setAdapter(adapter);
        lstDiemthi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                try{
                    builder.setMessage("Hệ số 1: " + arrDiemthi.get(position).Diemhs1 + "\nHệ số 2: " + arrDiemthi.get(position).Diemhs2);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }catch (Exception ex){
                    dialog.show();
                }

            }
        });
        btreload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getarrDiemthi(arrDiemthi,ts);
            }
        });
        btDownload.setOnClickListener(new View.OnClickListener() {
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
                cell.setCellValue("Điểm HS1");

                cell=row.createCell(3);
                cell.setCellValue("Điểm HS2");

                for (int i=0;i<arrDiemthi.size();i++){

                    QLDiemthi diemthi=arrDiemthi.get(i);
                    row=sheet.createRow(i+1);
                    cell=row.createCell(0);
                    cell.setCellValue(diemthi.Idsv);
                    cell=row.createCell(1);
                    cell.setCellValue(diemthi.Namesv);
                    cell=row.createCell(2);
                    cell.setCellValue(diemthi.Diemhs1);
                    cell=row.createCell(3);
                    cell.setCellValue(diemthi.Diemhs2);
                }
                int i=0;
                try {
                    while (filePath.exists()){
                        filePath=new File(Environment.getExternalStorageDirectory()+"/DSDiem("+i+").xls");
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
                            Toast.makeText(QuanLyDiemThi.this, "Download thành công!/DSDiem.xls", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(QuanLyDiemThi.this, "Download thành công!/DSDiem("+(i-1)+").xls", Toast.LENGTH_LONG).show();



            }
        });



    }

    private void anhxa() {
        btDownload=findViewById(R.id.imgDownloaddiemsv);
        lstDiemthi=findViewById(R.id.lstDiemthi);
        btreload=findViewById(R.id.imgReloaddiemsv);
    }
    void getThisinh(ArrayList<String> tts){
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
        mRef.child("QLDiemthi").child(mon).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    tts.add(snap.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getarrDiemthi(ArrayList<QLDiemthi> arrdt,ArrayList<String> tts){
        for(String ten:tts){
            QLDiemthi diemthi=new QLDiemthi(mon,ten);
            arrdt.add(diemthi);
        }

    }

}