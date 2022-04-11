package com.example.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LineThiAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<QLBaithi> lstCauhoi;

    public LineThiAdapter() {
    }

    public LineThiAdapter(Context context, int layout, ArrayList<QLBaithi> lstCauhoi) {
        this.context = context;
        this.layout = layout;
        this.lstCauhoi = lstCauhoi;
    }

    @Override
    public int getCount() {
        return lstCauhoi.size();
    }

    @Override
    public Object getItem(int position) {
        return lstCauhoi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);
        TextView txtcauhoi;
        RadioButton rda,rdb,rdc,rdd;
        ImageView imgerror;
        RadioGroup radioGroup;

        txtcauhoi=convertView.findViewById(R.id.tvlinendch);
        rda=convertView.findViewById(R.id.rda);
        rdb=convertView.findViewById(R.id.rdb);
        rdc=convertView.findViewById(R.id.rdc);
        rdd=convertView.findViewById(R.id.rdd);
        imgerror=convertView.findViewById(R.id.imgbaoloidethi);


        imgerror.setVisibility(View.INVISIBLE);
        imgerror.setEnabled(false);

        txtcauhoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgerror.setEnabled(true);
                imgerror.setVisibility(View.VISIBLE);
            }
        });
        QLBaithi cauhoi=lstCauhoi.get(position);
        txtcauhoi.setText("Câu "+(position+1)+": "+cauhoi.Cauhoi);
        rda.setText(cauhoi.da1);
        rdb.setText(cauhoi.da2);
        rdc.setText(cauhoi.da3);
        rdd.setText(cauhoi.da4);
        imgerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QLCauhoi cauhoierror=new QLCauhoi(cauhoi.Monhoc,cauhoi.Dokho,cauhoi.Cauhoi,cauhoi.da1,cauhoi.da2,cauhoi.da3,cauhoi.da4,cauhoi.dad);
                DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("QLBaoloi");
                mRef.push().setValue(cauhoierror, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(error==null){
                            AlertDialog.Builder Builder=new AlertDialog.Builder(v.getContext());
                            Builder.setMessage("Đã báo lỗi!");
                            Builder.setCancelable(true);
                            Builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog=Builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        });
        rda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rda.isChecked())
                    lstCauhoi.set(position,new QLBaithi(cauhoi.Cauhoi,cauhoi.da1,cauhoi.da2,cauhoi.da3,cauhoi.da4,cauhoi.dad,rda.getText().toString()));
            }
        });
        rdb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rdb.isChecked())
                    lstCauhoi.set(position,new QLBaithi(cauhoi.Cauhoi,cauhoi.da1,cauhoi.da2,cauhoi.da3,cauhoi.da4,cauhoi.dad,rdb.getText().toString()));
            }
        });
        rdc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rdc.isChecked())
                    lstCauhoi.set(position,new QLBaithi(cauhoi.Cauhoi,cauhoi.da1,cauhoi.da2,cauhoi.da3,cauhoi.da4,cauhoi.dad,rdc.getText().toString()));
            }
        });
        rdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rdd.isChecked())
                    lstCauhoi.set(position,new QLBaithi(cauhoi.Cauhoi,cauhoi.da1,cauhoi.da2,cauhoi.da3,cauhoi.da4,cauhoi.dad,rdd.getText().toString()));
            }
        });

        return convertView;
    }
}
