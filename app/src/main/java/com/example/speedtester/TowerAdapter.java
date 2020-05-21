package com.example.speedtester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TowerAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] towerLevels;
    int[] towernumAP;
//    String[] towerwarning;

    public TowerAdapter(Context c, String[] l, int[] a){
        towerLevels = l;
        towernumAP = a;

        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return towerLevels.length;
    }

    @Override
    public Object getItem(int position) {
        return towerLevels[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.tower_a_levels_detail,null);

        TextView towerLevelname = (TextView) v.findViewById(R.id.towerLevelName);
        TextView numAPTextView = (TextView) v.findViewById(R.id.numAPTextView);
        TextView warningTextView = (TextView) v.findViewById(R.id.warningTextView);

        String level = towerLevels[position];

        int AP = towernumAP[position];
//        String warning = towerAwarning[position];

        towerLevelname.setText(level);
        numAPTextView.setText("AP: "+AP);
//        warningTextView.setText("Warning: "+ warning);

        return v;
    }
}
