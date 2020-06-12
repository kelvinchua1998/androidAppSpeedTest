package com.example.speedtester.building_list;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.speedtester.R;
import com.example.speedtester.building_list.BuildingActivity;

public class buildingAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    String[] buildingName;
    int[] buildingLevel;
    int[] buildingAP;
    int[] BuildingWarning;
    int[] BuildingCritical;
    int[] buildingDownload;
    int[] buildingUpload;

    //to do: warning and critical

    public buildingAdapter(Context c, String[] name, int[] level, int[] AP, int[] warning, int[] critical, int[] download, int[] upload){
        buildingName = name;
        buildingLevel = level;
        buildingAP = AP;
        BuildingWarning = warning;
        BuildingCritical = critical;
        buildingDownload = download;
        buildingUpload= upload;


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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.building_listview_detail, parent,false);

        TextView towerNameTextView = (TextView) v.findViewById(R.id.towerNameTextView);
        TextView numberSsidTextView = (TextView) v.findViewById(R.id.totalAPTextView);
        TextView warningNumberTextView = (TextView) v.findViewById(R.id.warningNumTextView);
        TextView criticalNumberTextView = (TextView) v.findViewById(R.id.criticalNumTextView);
        TextView downloadTextView = (TextView) v.findViewById(R.id.averageDownloadTextView);
        TextView uploadTextView = (TextView) v.findViewById(R.id.averageUploadTextView);

        String Name = buildingName[position];
        int Level = buildingLevel[position];
        int NumAp;

        if(buildingAP != null){
            NumAp = buildingAP[position];}
        else{
            NumAp=0;
        }

        int warningNum = BuildingWarning[position];
        int criticalNum = BuildingCritical[position];
        int download = buildingDownload[position];
        int upload = buildingUpload[position];

        towerNameTextView.setText(Name);


        numberSsidTextView.setText("AP: "+NumAp);

        warningNumberTextView.setText("Warning: "+warningNum);
        criticalNumberTextView.setText("Critical: "+criticalNum);

        downloadTextView.setText(download+" Mb/s");
        uploadTextView.setText(upload+" Mb/s");

        if(warningNum != 0){

            warningNumberTextView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.indicatorOrange));
            warningNumberTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BuildingActivity twr = new BuildingActivity();
                    twr.onWarningClick(v,position);
                }
            });
        }

        if(criticalNum != 0){
            criticalNumberTextView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.indicatorRed));
            criticalNumberTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BuildingActivity twr = new BuildingActivity();
                    twr.onCriticalClick(v,position);
                }
            });
        }
        //set alternate row colours

        if (position % 2 == 1) {
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            v.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        return v;
    }

    public void setbuildingAP(int[] AP, int[] warning, int[]critical, int[] download, int[] upload) {
        buildingAP = AP;
        BuildingWarning = warning;
        BuildingCritical = critical;
        buildingDownload = download;
        buildingUpload = upload;

        notifyDataSetChanged();
    }
}
