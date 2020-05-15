package com.example.speedtester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TowerAlevelAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] towerALevels;
    String[] towerAnumAP;
    String[] towerAwarning;
    public TowerAlevelAdapter(Context c, String[] l,String[] a,String[] w){
        towerALevels = l;
        towerAnumAP = a;
        towerAwarning = w;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return towerALevels.length;
    }

    @Override
    public Object getItem(int position) {
        return towerALevels[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.tower_a_levels_detail,null);

        TextView towerAlevelListView = (TextView) v.findViewById(R.id.towerAlevelListView);
        TextView numAPTextView = (TextView) v.findViewById(R.id.numAPTextView);
        TextView warningTextView = (TextView) v.findViewById(R.id.warningTextView);

        String level = towerALevels[position];
        String AP = towerAnumAP[position];
        String warning = towerAwarning[position];

        towerAlevelListView.setText(level);
        numAPTextView.setText("number of AP: "+AP);
        warningTextView.setText("Warning: "+ warning);

        return v;
    }
}
