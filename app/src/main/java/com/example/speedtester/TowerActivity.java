package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TowerActivity extends AppCompatActivity {

    ListView towerListView;
    String[] towerNames;
    String[] towerLevels;

    APData data;
    JSONArray APlist;
    String APListStrData;
    int buildingIndex;
    TextView warningTextView;
    final ArrayList<Integer> arrayIndex = new ArrayList<>();
    Context context = GlobalApplication.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_a_levels);

        Intent in = getIntent();
        Bundle extras = in.getExtras();
        buildingIndex = extras.getInt("com.example.speedtester.buildingIndex", -1);
        APListStrData = extras.getString("com.example.speedtester.data");

        try {
            APlist = new JSONArray(APListStrData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Resources res = getResources();
        towerListView = (ListView) findViewById(R.id.towerListView);
        towerNames = res.getStringArray(R.array.building_name);

        getSupportActionBar().setTitle(towerNames[buildingIndex]);

        if (buildingIndex == 0)
            towerLevels = res.getStringArray(R.array.Main_Block);
        else if (buildingIndex == 1)
            towerLevels = res.getStringArray(R.array.Podium_Block);

        try {
            data= getNumAp(buildingIndex,APlist,towerNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //get the length



        for (int i =0; i<data.numAPEachLevel.length; i++){
            if (data.numAPEachLevel[i]!=0)
                arrayIndex.add(i);
        }

        String[] filteredTowerLevels = new String[arrayIndex.size()];
        int[] filtnumAPEachLevel = new int[arrayIndex.size()];
        int[] filtnormalEachLevel = new int[arrayIndex.size()];
        int[] filtwarningEachLevel = new int[arrayIndex.size()];
        int[] filtcriticalEachLevel= new int[arrayIndex.size()];
        int[] filtdownloadEachLevel = new int[arrayIndex.size()];
        int[] filtuploadEachLevel = new int[arrayIndex.size()];

        for (int i = 0; i<arrayIndex.size();i++){

            filteredTowerLevels[i] =towerLevels[arrayIndex.get(i)];
            filtnumAPEachLevel[i] = data.numAPEachLevel[arrayIndex.get(i)];
            filtnormalEachLevel[i] = data.normalEachLevel[arrayIndex.get(i)];
            filtwarningEachLevel[i] = data.warningEachLevel[arrayIndex.get(i)];
            filtcriticalEachLevel[i] = data.criticalEachLevel[arrayIndex.get(i)];
            filtdownloadEachLevel[i] = data.downloadEachLevel[arrayIndex.get(i)];
            filtuploadEachLevel[i] = data.uploadEachLevel[arrayIndex.get(i)];


        }
        TowerAdapter towerlevelAdapter = new TowerAdapter(this, filteredTowerLevels, filtnumAPEachLevel,filtnormalEachLevel, filtwarningEachLevel,filtcriticalEachLevel,filtdownloadEachLevel,filtuploadEachLevel);
        towerListView.setAdapter(towerlevelAdapter);

        // Making variables global
        data.APListStrData = APListStrData;
        data.arrayIndex = arrayIndex;
        data.buildingIndex = buildingIndex;
        data.context = getApplicationContext();

        towerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent showSsidActivity = new Intent(getApplicationContext(), ssidActivity.class);

                    Bundle extras = new Bundle();
                    extras.putString("from", "listView");
                    extras.putString("com.example.speedtester.data", APListStrData);
                    int trueLevel = arrayIndex.get(position);
                    extras.putInt("com.example.speedtester.level", trueLevel); //pass the postion to next screen
                    extras.putIntegerArrayList("com.example.speedtester.APIndex",data.indexAPEachLevel[trueLevel]);
                    extras.putInt("com.example.speedtester.buildingIndex", buildingIndex);

                    showSsidActivity.putExtras(extras);
                    startActivity(showSsidActivity);
                }
        });
    }

    private APData getNumAp(int buildingIndex, JSONArray APlist, String[] buildingName) throws JSONException {
        int i,level;
        ArrayList<Integer>[] indexAPEachLevel = new ArrayList[11];// An arrayList containing array list

        int[] numAPEachLevel = new int[] {0,0,0,0,0,0,0,0,0,0,0};
        int[] warningEachLevel = new int[] {0,0,0,0,0,0,0,0,0,0,0};
        int[] criticalEachLevel = new int[] {0,0,0,0,0,0,0,0,0,0,0};
        int[] normalEachLevel = new int[] {0,0,0,0,0,0,0,0,0,0,0};
        int[] downloadEachLevel = new int[] {0,0,0,0,0,0,0,0,0,0,0};
        int[] uploadEachLevel = new int[] {0,0,0,0,0,0,0,0,0,0,0};

        //initializing the array with ArrayList
        for (i = 0; i < indexAPEachLevel.length; i++) {
            indexAPEachLevel[i] = new ArrayList<>();
        }

        for (i=0 ; i<APlist.length();i++) {

            JSONObject singleAP = APlist.getJSONObject(i);
            JSONObject location = singleAP.getJSONObject("location");

            if (buildingIndex==0){
                if (location.get("building").toString().equals(buildingName[0])){

                    Log.d("data", "level:"+ location.getInt("level") + "ssid: "+ singleAP.getString("ssid"));
                    level = location.getInt("level");
                    if(level ==-1){
                        //total number of ap
                        numAPEachLevel[0] += 1;
                        //get index of ap in APlist for easy ref in the next page
                        indexAPEachLevel[0].add(i);
                        //collate total number of ap for basement for each status
                        if(singleAP.getInt("status")==1) warningEachLevel[0]++;
                        else if (singleAP.getInt("status")==2) criticalEachLevel[0]++;
                        else if (singleAP.getInt("status")==0) normalEachLevel[0]++;
                        //get sum of download and upload
                        downloadEachLevel[0] += singleAP.getJSONObject("last_speedtest").getInt("download");
                        uploadEachLevel[0] += singleAP.getJSONObject("last_speedtest").getInt("upload");
                    }

                    else{
                        numAPEachLevel[level] += 1;
                        indexAPEachLevel[level].add(i);
                        if(singleAP.getInt("status")==1) warningEachLevel[level]++;
                        else if (singleAP.getInt("status")==2) criticalEachLevel[level]++;
                        else if (singleAP.getInt("status")==0) normalEachLevel[level]++;

                        downloadEachLevel[level] += singleAP.getJSONObject("last_speedtest").getInt("download");
                        uploadEachLevel[level] += singleAP.getJSONObject("last_speedtest").getInt("upload");
                    }

                }

            }
            else if (buildingIndex==1){
                if (location.get("building").toString().equals(buildingName[1])){
                    level = location.getInt("level");
                    if(level == -1){
                        numAPEachLevel[0] += 1;
                        indexAPEachLevel[0].add(i);
                        if(singleAP.getInt("status")==1) warningEachLevel[0]++;
                        else if (singleAP.getInt("status")==2) criticalEachLevel[0]++;
                        else if (singleAP.getInt("status")==0) normalEachLevel[0]++;

                        downloadEachLevel[0] += singleAP.getJSONObject("last_speedtest").getInt("download");
                        uploadEachLevel[0] += singleAP.getJSONObject("last_speedtest").getInt("upload");
                    }

                    else{
                        numAPEachLevel[level] += 1;
                        indexAPEachLevel[level].add(i);
                        if(singleAP.getInt("status")==1) warningEachLevel[level]++;
                        else if (singleAP.getInt("status")==2) criticalEachLevel[level]++;
                        else if (singleAP.getInt("status")==0) normalEachLevel[level]++;

                        downloadEachLevel[level] += singleAP.getJSONObject("last_speedtest").getInt("download");
                        uploadEachLevel[level] += singleAP.getJSONObject("last_speedtest").getInt("upload");
                    }

                }
            }
        }

        //get mean of download and upload for each level
        for (i =0; i < numAPEachLevel.length;i++){
            if (numAPEachLevel[i] == 0)
            {
                downloadEachLevel[i]=0;
                uploadEachLevel[i]=0;
            }
            else
                {
                downloadEachLevel[i] /= numAPEachLevel[i];
                uploadEachLevel[i] /= numAPEachLevel[i];
            }

        }



        Log.d("data", "getNumAp: calculated ");

        //return a class that contain all the data
        APData data = new APData();

        data.numAPEachLevel = numAPEachLevel;
        data.indexAPEachLevel = indexAPEachLevel;
        data.warningEachLevel = warningEachLevel;
        data.criticalEachLevel = criticalEachLevel;
        data.normalEachLevel = normalEachLevel;
        data.downloadEachLevel = downloadEachLevel;
        data.uploadEachLevel = uploadEachLevel;

        return data;

    }

    public void onWarningClick(View v,int position) {

        Intent showWarningAP = new Intent(context, ssidActivity.class);

        Bundle extras = new Bundle();
        extras.putString("from", "warning");

        int trueLevel = data.arrayIndex.get(position);
        extras.putIntegerArrayList("com.example.speedtester.APIndex",data.indexAPEachLevel[trueLevel]);

        extras.putString("com.example.speedtester.data", data.APListStrData);

        extras.putInt("com.example.speedtester.level", trueLevel); //pass the postion to next screen
        extras.putInt("com.example.speedtester.buildingIndex", data.buildingIndex);

        showWarningAP.putExtras(extras);
        v.getContext().startActivity(showWarningAP);
    }

    public void onCriticalClick(View v, int position) {
        Intent showWarningAP = new Intent(context, ssidActivity.class);

        Bundle extras = new Bundle();
        extras.putString("from", "critical");

        int trueLevel = data.arrayIndex.get(position);
        extras.putIntegerArrayList("com.example.speedtester.APIndex",data.indexAPEachLevel[trueLevel]);

        extras.putString("com.example.speedtester.data", data.APListStrData);

        extras.putInt("com.example.speedtester.level", trueLevel); //pass the postion to next screen
        extras.putInt("com.example.speedtester.buildingIndex", data.buildingIndex);

        showWarningAP.putExtras(extras);
        v.getContext().startActivity(showWarningAP);
    }

    public static class APData{
        public static ArrayList<Integer>[] indexAPEachLevel;
        public static Context context;
        int[] numAPEachLevel;
        int[] warningEachLevel;
        int[] criticalEachLevel;
        int[] normalEachLevel;
        int[] uploadEachLevel;
        int[] downloadEachLevel;

        public static ArrayList<Integer> arrayIndex;
        public static String APListStrData;
        public static int buildingIndex;


    }


}
