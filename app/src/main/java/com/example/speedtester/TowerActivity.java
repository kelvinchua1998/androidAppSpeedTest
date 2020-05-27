package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

        TowerAdapter towerlevelAdapter = new TowerAdapter(this, towerLevels, data.numAPEachLevel,data.normalEachLevel, data.warningEachLevel,data.criticalEachLevel,data.downloadEachLevel,data.uploadEachLevel);
        towerListView.setAdapter(towerlevelAdapter);

        towerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent showSsidActivity = new Intent(getApplicationContext(), ssidActivity.class);

                    Bundle extras = new Bundle();

                    extras.putString("com.example.speedtester.data", APListStrData);
                    extras.putInt("com.example.speedtester.level", position); //pass the postion to next screen
                    extras.putIntegerArrayList("com.example.speedtester.APIndexEachLevel",data.indexAPEachLevel[position]);
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
                    if(level ==-1){
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
            downloadEachLevel[i] /= numAPEachLevel[i];
            uploadEachLevel[i] /= numAPEachLevel[i];
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

    public class APData{
        ArrayList<Integer>[] indexAPEachLevel;
        int[] numAPEachLevel;
        int[] warningEachLevel;
        int[] criticalEachLevel;
        int[] normalEachLevel;
        int[] uploadEachLevel;
        int[] downloadEachLevel;
    }
}
