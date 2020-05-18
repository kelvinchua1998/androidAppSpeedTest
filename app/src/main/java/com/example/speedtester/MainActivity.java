package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView BuildingListView;
    String[] buildingName;
    String[] buildingLevel;
    String[] buildingNumSsid;
    String[] buildingWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        BuildingListView = (ListView)findViewById(R.id.buldingListView);
        buildingName = res.getStringArray(R.array.building_name);
        buildingLevel = res.getStringArray(R.array.building_levels);
        buildingNumSsid = res.getStringArray(R.array.building_num_ssid);
        buildingWarning = res.getStringArray(R.array.building_warning);

        buildingAdapter BuildingAdapter = new buildingAdapter(this,buildingName,buildingLevel,buildingNumSsid,buildingWarning);
        BuildingListView.setAdapter(BuildingAdapter);


        BuildingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showDetailActivity = new Intent(getApplicationContext(), towerALevelsActivity.class);
                showDetailActivity.putExtra("com.example.speedtester.buildingIndex", position);//pass the postion to next screen
                startActivity(showDetailActivity);
            }
        });
    }
}
