package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class towerALevelsActivity extends AppCompatActivity {

    ListView towerAListView;
    String[] towerALevels;
    String[] towerAnumAP;
    String[] towerAwarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_a_levels);

        Intent in = getIntent();
        int index = in.getIntExtra("com.example.speedtester.buildingIndex", -1);

        Resources res = getResources();
        towerAListView = (ListView) findViewById(R.id.towerAListView);
        towerALevels = res.getStringArray(R.array.tower_a_levels);
//            towerAnumAP = res.getStringArray(R.array.tower_a_numAP);
//            towerAwarning = res.getStringArray(R.array.tower_a_warning);

        TowerAlevelAdapter towerAlevelAdapter = new TowerAlevelAdapter(this, towerALevels, towerAnumAP, towerAwarning);
        towerAListView.setAdapter(towerAlevelAdapter);

        towerAListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showSsidActivity = new Intent(getApplicationContext(), ssidActivity.class);
                showSsidActivity.putExtra("com.example.speedtester.level", position);//pass the postion to next screen
                startActivity(showSsidActivity);
            }
        });

    }
}
