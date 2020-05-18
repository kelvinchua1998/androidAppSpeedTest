package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

public class ssidActivity extends AppCompatActivity {
    RecyclerView ssidRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssid);

        Intent in = getIntent();
        int levelindex = in.getIntExtra("com.example.speedtester.level", -1);


            Resources res = getResources();

            TypedArray mockdata = res.obtainTypedArray(R.array.mock_data);
            int n = mockdata.length();
            String[][] mockData_array = new String[n][];
            for (int i = 0; i < n; ++i) {
                int id = mockdata.getResourceId(i, 0);
                if (id > 0) {
                    mockData_array[i] = res.getStringArray(id);
                } else {
                    // something wrong with the XML
                }
            }
            mockdata.recycle();

            ssidRecyclerView = (RecyclerView) findViewById(R.id.ssidRecyclerView);
//            mockdata = res.getStringArray(R.array.mock_data);


            ssidAdapter ssidAdapter = new ssidAdapter(this, mockData_array);
            ssidRecyclerView.setAdapter(ssidAdapter);
            ssidRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
