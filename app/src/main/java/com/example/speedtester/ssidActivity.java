package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ssidActivity extends AppCompatActivity implements ssidAdapter.RecyclerViewClickListener {
    RecyclerView ssidRecyclerView;

    String APListStrData;
    int levelIndex;
    ArrayList<Integer> APIndex;
    JSONArray APlist;
    String[] towerNames;
    String[] levelNames;
    int buildingIndex;
    ssidData data;
    String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssid);

        Intent in = getIntent();
        Bundle extras = in.getExtras();

        //get data in
        from = extras.getString("from");
        levelIndex = extras.getInt("com.example.speedtester.level", -1);
        APListStrData = extras.getString("com.example.speedtester.data");
        APIndex = extras.getIntegerArrayList("com.example.speedtester.APIndex");
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

        if(from.equals("buildingWarning") || from.equals("buildingCritical"))
        {
            getSupportActionBar().setTitle(towerNames[buildingIndex]);
        }else{
            getSupportActionBar().setTitle(towerNames[buildingIndex]);
            getSupportActionBar().setSubtitle(levelNames[levelIndex]);
        }


        //get ap index for the level and the ap list for the level

        data = getData(APIndex, APlist,from,buildingIndex);


        ssidRecyclerView = (RecyclerView) findViewById(R.id.ssidRecyclerView);

        ssidAdapter ssidAdapter = new ssidAdapter(this, data.ssidList, data.lastSpeedtest, data.statusList,data.runtimeList,this);
        ssidRecyclerView.setAdapter(ssidAdapter);
        ssidRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ssidRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // Get drawable object
        Drawable mDivider = ContextCompat.getDrawable(this, R.drawable.divider);
        // Create a DividerItemDecoration whose orientation is vertical
        DividerItemDecoration vItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        // Set the drawable on it
        vItemDecoration.setDrawable(mDivider);
    }


    private ssidData getData(ArrayList<Integer> APIndex, JSONArray APlist,String from,int buildingIndex){
        ArrayList<String> ssidList = new ArrayList<>();
        ArrayList<JSONObject> lastSpeedtest = new ArrayList<>();
        ArrayList<Integer> statusList = new ArrayList<>();
        ArrayList<Integer> runtimeList = new ArrayList<>();
        for(int i=0; i<APIndex.size();i++){

            JSONObject singleAP = null;

            try {
                singleAP = APlist.getJSONObject(APIndex.get(i));
                if (from.equals("listView")){
                    ssidList.add(singleAP.getString("ssid"));
                    lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                    statusList.add(singleAP.getInt("status"));
                    runtimeList.add(singleAP.getInt("runtime"));
                }
                else if (from.equals("warning")){
                    // the AP into the data if the status is warning
                    if (singleAP.getInt("status")==1){
                        ssidList.add(singleAP.getString("ssid"));
                        lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                        statusList.add(singleAP.getInt("status"));
                        runtimeList.add(singleAP.getInt("runtime"));
                    }
                }
                else if (from.equals("critical")){
                    // the AP into the data if the status is warning
                    if (singleAP.getInt("status")==2){
                        ssidList.add(singleAP.getString("ssid"));
                        lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                        statusList.add(singleAP.getInt("status"));
                        runtimeList.add(singleAP.getInt("runtime"));
                    }
                }
                else if (from.equals("buildingWarning")){
                    // the AP into the data if the status is warning
                    if (singleAP.getInt("status")==1){
                        if (buildingIndex == 0){
                            if(singleAP.getJSONObject("location").getString("building").equals("Main Block")){
                                ssidList.add(singleAP.getString("ssid"));
                                lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                                statusList.add(singleAP.getInt("status"));
                                runtimeList.add(singleAP.getInt("runtime"));
                            }
                        }else if(buildingIndex == 1){
                            if(singleAP.getJSONObject("location").getString("building").equals("Podium Block")){
                                ssidList.add(singleAP.getString("ssid"));
                                lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                                statusList.add(singleAP.getInt("status"));
                                runtimeList.add(singleAP.getInt("runtime"));
                            }
                        }

                    }
                }
                else if (from.equals("buildingCritical")){
                    // the AP into the data if the status is warning
                    if (singleAP.getInt("status")==2){
                        if (buildingIndex == 0){
                            if(singleAP.getJSONObject("location").getString("building").equals("Main Block")){
                                ssidList.add(singleAP.getString("ssid"));
                                lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                                statusList.add(singleAP.getInt("status"));
                                runtimeList.add(singleAP.getInt("runtime"));
                            }
                        }else if(buildingIndex == 1){
                            if(singleAP.getJSONObject("location").getString("building").equals("Podium Block")){
                                ssidList.add(singleAP.getString("ssid"));
                                lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                                statusList.add(singleAP.getInt("status"));
                                runtimeList.add(singleAP.getInt("runtime"));
                            }
                        }

                    }
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ssidData data = new ssidData();
        data.ssidList = ssidList;
        data.lastSpeedtest = lastSpeedtest;
        data.statusList = statusList;
        data.runtimeList = runtimeList;
        return data;
    }

    @Override
    public void onClick(int position) {
        Intent showAPDetail = new Intent(getApplicationContext(),showApDetails.class);

        Bundle extras = new Bundle();

        extras.putInt("com.example.speedtester.APIndex", APIndex.get(position));
        extras.putString("com.example.speedtester.data", APListStrData);

        showAPDetail.putExtras(extras);
        startActivity(showAPDetail);
    }

    public class ssidData{
        ArrayList<String> ssidList;
        ArrayList<JSONObject> lastSpeedtest;
        ArrayList<Integer> statusList;
        ArrayList<Integer> runtimeList;
    }

}
