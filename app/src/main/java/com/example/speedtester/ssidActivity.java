package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ssidActivity extends AppCompatActivity {
    RecyclerView ssidRecyclerView;

    String APListStrData;
    int levelIndex;
    ArrayList APIndexSingleLevel;
    JSONArray APlist;
    String[] towerNames;
    String[] levelNames;
    int buildingIndex;
    ssidData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssid);

        Intent in = getIntent();
        Bundle extras = in.getExtras();

        //get data in
        levelIndex = extras.getInt("com.example.speedtester.level", -1);
        APListStrData = extras.getString("com.example.speedtester.data");
        APIndexSingleLevel = extras.getIntegerArrayList("com.example.speedtester.APIndexEachLevel");
        buildingIndex = extras.getInt("com.example.speedtester.buildingIndex", -1);

        // parse data into APlist
        try {
            APlist = new JSONArray(APListStrData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Resources res = getResources();

        //set title and sub title
        towerNames = res.getStringArray(R.array.building_name);
        if (buildingIndex == 0)
            levelNames = res.getStringArray(R.array.Main_Block);
        else if (buildingIndex == 1)
            levelNames = res.getStringArray(R.array.Podium_Block);
        getSupportActionBar().setTitle(towerNames[buildingIndex]);
        getSupportActionBar().setSubtitle(levelNames[levelIndex]);

        //get ap index for the level and the ap list for the level

        data = getData(APIndexSingleLevel, APlist);


        ssidRecyclerView = (RecyclerView) findViewById(R.id.ssidRecyclerView);

        ssidAdapter ssidAdapter = new ssidAdapter(this, data.ssidList, data.lastSpeedtest, data.statusList);
        ssidRecyclerView.setAdapter(ssidAdapter);
        ssidRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private ssidData getData(ArrayList<Integer> APIndexSingleLevel, JSONArray APlist){
        ArrayList<String> ssidList = new ArrayList<>();
        ArrayList<JSONObject> lastSpeedtest = new ArrayList<>();
        ArrayList<Integer> statusList = new ArrayList<>();

        for(int i=0; i<APIndexSingleLevel.size();i++){

            JSONObject singleAP = null;

            try {
                singleAP = APlist.getJSONObject(APIndexSingleLevel.get(i));

                ssidList.add(singleAP.getString("ssid"));
                lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                statusList.add(singleAP.getInt("status"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ssidData data = new ssidData();
        data.ssidList = ssidList;
        data.lastSpeedtest = lastSpeedtest;
        data.statusList = statusList;
        return data;
    }

    public class ssidData{
        ArrayList<String> ssidList;
        ArrayList<JSONObject> lastSpeedtest;
        ArrayList<Integer> statusList;
    }
}
