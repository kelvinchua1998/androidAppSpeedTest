package com.example.speedtester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class buildingAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    String[] buildingName;
    String[] buildingLevel;
    String[] buildingNumSsid;
    String[] buildingWarning;

    public buildingAdapter(Context c,String[] n,String[] l, String[] s, String[] w){
        buildingName = n;
        buildingLevel = l;
        buildingNumSsid = s;
        buildingWarning = w;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return buildingName.length;
    }

    @Override
    public Object getItem(int position) {
        return buildingName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.building_listview_detail, null);

        TextView towerNameTextView = (TextView) v.findViewById(R.id.towerNameTextView);
        TextView numberLevelsTextView = (TextView) v.findViewById(R.id.numberLevelsTextView);
        TextView numberSsidTextView = (TextView) v.findViewById(R.id.numberSsidTextView);
        TextView warningNumberTextView = (TextView) v.findViewById(R.id.warningNumberTextView);


        String Name = buildingName[position];
        String Level = buildingLevel[position];
        String NumSsid = buildingNumSsid[position];
        String Warning = buildingWarning[position];

        towerNameTextView.setText(Name);
        numberLevelsTextView.setText("number of levels: "+Level);
        numberSsidTextView.setText("number of AP: "+NumSsid);
        warningNumberTextView.setText("WARNING: "+Warning);
        return v;
    }
}
