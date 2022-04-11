package com.example.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adminAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<adminaccount> lstAdmin;

    public adminAdapter(Context context, ArrayList<adminaccount> lstAdmin) {
        this.context = context;
        this.lstAdmin = lstAdmin;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.line_admin,null);

        TextView user=convertView.findViewById(R.id.txtPassad);
        TextView pass=convertView.findViewById(R.id.txtnameAd);

        user.setText(lstAdmin.get(position).username);
        pass.setText(lstAdmin.get(position).password);
        return convertView;
    }
}
