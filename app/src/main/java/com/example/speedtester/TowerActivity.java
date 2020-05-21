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

public class TowerActivity extends AppCompatActivity {

    ListView towerListView;
    String[] towerNames;
    String[] towerLevels;
    int[] towerNumAP = new int[] {0,0,0,0,0,0,0,0,0,0,0};
    String[] towerWarning;

    JSONArray APlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_a_levels);

        Intent in = getIntent();
        Bundle extras = in.getExtras();
        int buildingIndex = extras.getInt("com.example.speedtester.buildingIndex", -1);
        String Data = extras.getString("com.example.speedtester.data");
        try {
            APlist = new JSONArray(Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Resources res = getResources();
        towerListView = (ListView) findViewById(R.id.towerListView);
        towerNames = res.getStringArray(R.array.building_name);

        if (buildingIndex == 0)
            towerLevels = res.getStringArray(R.array.Main_Block);
        else if (buildingIndex == 1)
            towerLevels = res.getStringArray(R.array.Podium_Block);

        try {
            towerNumAP = getNumAp(buildingIndex,APlist,towerNames);
            Log.d("data", "towerNumAP: "+towerNumAP.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TowerAdapter towerlevelAdapter = new TowerAdapter(this, towerLevels, towerNumAP);
        towerListView.setAdapter(towerlevelAdapter);

        towerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent showSsidActivity = new Intent(getApplicationContext(), ssidActivity.class);
                    showSsidActivity.putExtra("com.example.speedtester.level", position);//pass the postion to next screen
                    startActivity(showSsidActivity);
                }
        });
    }

    private int[] getNumAp(int buildingIndex, JSONArray APlist, String[] buildingName) throws JSONException {
        int i,level;

        int[] numAPEachLevel = new int[] {0,0,0,0,0,0,0,0,0,0,0};

        for (i=0 ; i<APlist.length();i++) {

            JSONObject singleAP = APlist.getJSONObject(i);
            JSONObject location = singleAP.getJSONObject("location");
            if (buildingIndex==0){
                if (location.get("building").toString().equals(buildingName[0])){
                    Log.d("data", "level:"+ location.getInt("level") + "ssid: "+ singleAP.getString("ssid"));
                    level = location.getInt("level");
                    if(level ==-1)
                        numAPEachLevel[0] += 1;
                    else
                        numAPEachLevel[level] += 1;
                }

            }
            else if (buildingIndex==1){
                if (location.get("building").toString().equals(buildingName[1])){
                    level = location.getInt("level");
                    if(level ==-1)
                        numAPEachLevel[0] += 1;
                    else
                        numAPEachLevel[level] += 1;
                }
            }
        }
        Log.d("data", "getNumAp: calculated ");
        return numAPEachLevel;

    }


}
