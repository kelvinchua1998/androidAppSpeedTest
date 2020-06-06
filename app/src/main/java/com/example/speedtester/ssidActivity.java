package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ssidActivity extends AppCompatActivity implements ssidAdapter.RecyclerViewClickListener {
    RecyclerView ssidRecyclerView;

    String APListStrData;
    int levelIndex;
    ArrayList<Integer> deviceIDIndex;
    JSONArray APlist;
    String[] towerNames;
    String[] levelNames;
    int buildingIndex;
    ssidData data;
    String from;
    SwipeRefreshLayout refreshLayout;
    GlobalApplication.Config config = GlobalApplication.getconfiq();
    GlobalApplication.Data shareddata = GlobalApplication.getData();

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
//        APIndex = extras.getIntegerArrayList("com.example.speedtester.APIndex");
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

        deviceIDIndex = getDeviceIDIndex(APlist,buildingIndex,levelIndex,towerNames);

        data = getData(deviceIDIndex, APlist);


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

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.ssidRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ssidRefresh();
            }
        });
    }

    @Override
    protected void onResume() {
        GlobalApplication.Data apdata = GlobalApplication.getData();
        String shareAPlist = apdata.APlist;
        if(!APListStrData.equals(shareAPlist))
            recreate();
        super.onResume();
    }

    private void ssidRefresh() {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject bodyoptions = new JSONObject();

        try {
            bodyoptions.put("token", config.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String options = bodyoptions.toString();
        RequestBody body = RequestBody.create(mediaType, options);
        Request request = new Request.Builder()
                .url(config.getAPlisturl)
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("response test", "FAILEED");
                Log.d("response test", e.getMessage());
                e.printStackTrace();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    Log.d("response test", "WORKED");
                    Log.d("response test", myresponse);


                    try {
                        JSONObject jsonData = new JSONObject(myresponse);
                        APlist = jsonData.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    shareddata.APlist = APlist.toString();

                    Intent refreshSsidActivity = new Intent(getApplicationContext(), ssidActivity.class);

                    Bundle extras = new Bundle();

                    extras.putString("from", from);
                    extras.putString("com.example.speedtester.data", APlist.toString());
                    extras.putInt("com.example.speedtester.level", levelIndex); //pass the postion to next screen
//                    extras.putIntegerArrayList("com.example.speedtester.APIndex",data.indexAPEachLevel[trueLevel]);
                    extras.putInt("com.example.speedtester.buildingIndex", buildingIndex);

                    refreshSsidActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//remove animation
                    finish();// end the current activity
                    overridePendingTransition(0,0);//remove animation

                    refreshSsidActivity.putExtras(extras);
                    startActivity(refreshSsidActivity);

                    refreshLayout.setRefreshing(false);
//
                }
            }

        });
    }

    private ArrayList<Integer> getDeviceIDIndex(JSONArray aPlist, int buildingIndex, int levelIndex, String[] towerNames) {
        ArrayList<Integer> deviceIDIndex = new ArrayList<>();

        if (levelIndex == 0)
            levelIndex = -1;

        for(int i=0; i<aPlist.length();i++){

            try {
                if(from.equals("listView")){
                    JSONObject singleAP = aPlist.getJSONObject(i);
                    if( singleAP.getJSONObject("location").getString("building").equals(towerNames[buildingIndex])  && singleAP.getJSONObject("location").getInt("level") == levelIndex)
                        deviceIDIndex.add(singleAP.getInt("device_id"));
                }
                else if(from.equals("warning")){
                    JSONObject singleAP = aPlist.getJSONObject(i);
                    if( singleAP.getJSONObject("location").getString("building").equals(towerNames[buildingIndex])  && singleAP.getJSONObject("location").getInt("level") == levelIndex && singleAP.getInt("status")== 1)
                        deviceIDIndex.add(singleAP.getInt("device_id"));
                }
                else if(from.equals("critical")){
                    JSONObject singleAP = aPlist.getJSONObject(i);
                    if( singleAP.getJSONObject("location").getString("building").equals(towerNames[buildingIndex])  && singleAP.getJSONObject("location").getInt("level") == levelIndex && singleAP.getInt("status")== 2)
                        deviceIDIndex.add(singleAP.getInt("device_id"));
                }
                else if(from.equals("buildingWarning")){
                    JSONObject singleAP = aPlist.getJSONObject(i);
                    if( singleAP.getJSONObject("location").getString("building").equals(towerNames[buildingIndex])  && singleAP.getInt("status")== 1)
                        deviceIDIndex.add(singleAP.getInt("device_id"));
                }
                else if(from.equals("buildingCritical")){
                    JSONObject singleAP = aPlist.getJSONObject(i);
                    if( singleAP.getJSONObject("location").getString("building").equals(towerNames[buildingIndex])  &&  singleAP.getInt("status")== 2)
                        deviceIDIndex.add(singleAP.getInt("device_id"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return deviceIDIndex;
    }


    private ssidData getData(ArrayList<Integer> deviceIDIndex, JSONArray APlist){
        ArrayList<String> ssidList = new ArrayList<>();
        ArrayList<JSONObject> lastSpeedtest = new ArrayList<>();
        ArrayList<Integer> statusList = new ArrayList<>();
        ArrayList<Integer> runtimeList = new ArrayList<>();

        for(int i=0; i<deviceIDIndex.size();i++){

            JSONObject singleAP= null;

            try {

                for (int j = 0; j<APlist.length();j++){
                    singleAP = APlist.getJSONObject(j);
                    if (singleAP.getInt("device_id")==deviceIDIndex.get(i))
                        break;
                }

                ssidList.add(singleAP.getString("name"));
                lastSpeedtest.add(singleAP.getJSONObject("last_speedtest"));
                statusList.add(singleAP.getInt("status"));
                runtimeList.add(singleAP.getInt("runtime"));

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

        extras.putInt("com.example.speedtester.device_id", deviceIDIndex.get(position));
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
