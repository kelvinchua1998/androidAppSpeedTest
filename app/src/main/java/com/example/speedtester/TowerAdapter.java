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
    int[] towerWarning;
    int[] towerCritical;

    public TowerAdapter(Context c, String[] levelString, int[] APIntArray, int[] warningIntArray, int[] criticalIntArray){
        towerLevels = levelString;
        towernumAP = APIntArray;
        towerWarning = warningIntArray;
        towerCritical  = criticalIntArray;

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
        TextView criticalTextView = (TextView) v.findViewById(R.id.criticalTextView);

        String level = towerLevels[position];

        int AP = towernumAP[position];
        int warning = towerWarning[position];
        int critical = towerCritical[position];

        towerLevelname.setText(level);

        numAPTextView.setText("AP: " + AP);
        warningTextView.setText("Warning: " + warning);
        criticalTextView.setText("critical: " + critical);

        return v;
    }
}
