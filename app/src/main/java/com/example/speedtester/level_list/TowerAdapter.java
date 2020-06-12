package com.example.speedtester.level_list;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.speedtester.R;
import com.example.speedtester.level_list.TowerActivity;

public class TowerAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] towerLevels;
    int[] towernumAP;
    int[] towerNormal;
    int[] towerWarning;
    int[] towerCritical;
    int[] towerDownload;
    int[] towerUpload;

    public TowerAdapter(Context c, String[] levelString, int[] APIntArray,int[] normalArray, int[] warningIntArray, int[] criticalIntArray, int[] downloadArray, int[] uploadArray){
        towerLevels = levelString;
        towernumAP = APIntArray;
        towerWarning = warningIntArray;
        towerCritical  = criticalIntArray;
        towerNormal = normalArray;
        towerDownload = downloadArray;
        towerUpload = uploadArray;

        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    public void towerRefresh(String[] refreshTowerLevels, int[] numAPEachLevel, int[] normalEachLevel, int[] warningEachLevel, int[] criticalEachLevel, int[] downloadEachLevel, int[] uploadEachLevel) {
        towerLevels = refreshTowerLevels;
        towernumAP = numAPEachLevel;
        towerWarning = warningEachLevel;
        towerCritical  = criticalEachLevel;
        towerNormal = normalEachLevel;
        towerDownload = downloadEachLevel;
        towerUpload = uploadEachLevel;

        notifyDataSetChanged();

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.tower_a_levels_detail,null);

        TextView towerLevelname = (TextView) v.findViewById(R.id.towerLevelName);
        TextView numAPTextView = (TextView) v.findViewById(R.id.numAPTextView);
//        TextView normalTextView = (TextView) v.findViewById(R.id.normalTextView);
        TextView warningTextView = (TextView) v.findViewById(R.id.warningTextView);
        TextView criticalTextView = (TextView) v.findViewById(R.id.criticalTextView);
        TextView downloadTextView = (TextView) v.findViewById(R.id.towerAveDownload);
        TextView uploadTextView = (TextView) v.findViewById(R.id.towerAveUpload);

        String level = towerLevels[position];

        int AP = towernumAP[position];
//        int normal = towerNormal[position];
        int warning = towerWarning[position];
        int critical = towerCritical[position];
        int download = towerDownload[position];
        int upload = towerUpload[position];

        towerLevelname.setText(level);

        numAPTextView.setText("Total:      " + AP);
//        normalTextView.setText("Normal: "+ normal);
        warningTextView.setText("Warning: " + warning);
        criticalTextView.setText("Critical:   " + critical);
        downloadTextView.setText(download +" Mb/s");
        uploadTextView.setText(upload +" Mb/s");

        //set alternate row colour
        if (position % 2 == 1) {
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            v.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }


        if(warning != 0){

            warningTextView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.indicatorOrange));
            warningTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TowerActivity twr = new TowerActivity();
                    twr.onWarningClick(v,position);
                }
            });
        }

        if(critical != 0){
            criticalTextView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.indicatorRed));
            criticalTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TowerActivity twr = new TowerActivity();
                    twr.onCriticalClick(v,position);
                }
            });
        }
        return v;
    }

}
