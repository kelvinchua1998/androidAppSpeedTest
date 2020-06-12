package com.example.speedtester.level_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.speedtester.GlobalApplication;
import com.example.speedtester.R;
import com.example.speedtester.ssid_list.ssidActivity;

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

public class TowerActivity extends AppCompatActivity {

    ListView towerListView;
    String[] towerNames;
    String[] towerLevels;
    TowerAdapter towerlevelAdapter;
    APData data;
    JSONArray APlist;
    String APListStrData;
    int buildingIndex;
    TextView warningTextView;
    GlobalApplication.Config config = GlobalApplication.getconfiq();
    Context context = GlobalApplication.getAppContext();
    GlobalApplication.Data shareddata = GlobalApplication.getData();

    SwipeRefreshLayout refreshLayout;
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


        data = filterAP(towerLevels,data.numAPEachLevel,data.normalEachLevel,data.warningEachLevel, data.criticalEachLevel,data.downloadEachLevel,data.uploadEachLevel);




        towerlevelAdapter = new TowerAdapter(this, data.towerLevels, data.numAPEachLevel, data.normalEachLevel, data.warningEachLevel,data.criticalEachLevel,data.downloadEachLevel,data.uploadEachLevel);
        towerListView.setAdapter(towerlevelAdapter);

        // Making variables global
        data.APListStrData = APListStrData;

        data.buildingIndex = buildingIndex;
        data.context = getApplicationContext();

        refreshLayout = findViewById(R.id.towerRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                towerRefresh();

            }
        });

        towerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent showSsidActivity = new Intent(getApplicationContext(), ssidActivity.class);

                    Bundle extras = new Bundle();
                    extras.putString("from", "listView");
                    extras.putString("com.example.speedtester.data", data.APListStrData);
                    int trueLevel = data.arrayIndex.get(position);
                    extras.putInt("com.example.speedtester.level", trueLevel); //pass the postion to next screen
                    extras.putIntegerArrayList("com.example.speedtester.APIndex",data.indexAPEachLevel[trueLevel]);
                    extras.putInt("com.example.speedtester.buildingIndex", data.buildingIndex);

                    showSsidActivity.putExtras(extras);
                    startActivity(showSsidActivity);
                }
        });
    }

    @Override
    protected void onResume() {
        GlobalApplication.Data shareddata = GlobalApplication.getData();

        if(!data.APListStrData.equals(shareddata.APlist)){

            Intent refreshList = new Intent(getApplicationContext(), TowerActivity.class);
            Bundle extras = new Bundle();

            extras.putInt("com.example.speedtester.buildingIndex",data.buildingIndex);
            extras.putString("com.example.speedtester.data", shareddata.APlist);

            refreshList.putExtras(extras);
            refreshList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            finish();// end the current activity
            overridePendingTransition(0,0);
            startActivity(refreshList);//start the activity again

        }


        super.onResume();
    }

    private APData filterAP(String[] TowerLevels,int[] numAPEachLevel, int[] normalEachLevel, int[] warningEachLevel, int[] criticalEachLevel, int[] downloadEachLevel, int[] uploadEachLevel) {

        final ArrayList<Integer> arrayIndex = new ArrayList<>();

        for (int i =0; i<numAPEachLevel.length; i++){
            if (numAPEachLevel[i]!=0)
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

            filteredTowerLevels[i] =TowerLevels[arrayIndex.get(i)];
            filtnumAPEachLevel[i] = numAPEachLevel[arrayIndex.get(i)];
            filtnormalEachLevel[i] = normalEachLevel[arrayIndex.get(i)];
            filtwarningEachLevel[i] = warningEachLevel[arrayIndex.get(i)];
            filtcriticalEachLevel[i] = criticalEachLevel[arrayIndex.get(i)];
            filtdownloadEachLevel[i] = downloadEachLevel[arrayIndex.get(i)];
            filtuploadEachLevel[i] = uploadEachLevel[arrayIndex.get(i)];

        }
        data.towerLevels = filteredTowerLevels;
        data.numAPEachLevel = filtnumAPEachLevel;
//        data.indexAPEachLevel = indexAPEachLevel;
        data.normalEachLevel = filtnormalEachLevel;
        data.warningEachLevel = filtwarningEachLevel;
        data.criticalEachLevel = filtcriticalEachLevel;
        data.arrayIndex = arrayIndex;
        data.downloadEachLevel = filtdownloadEachLevel;
        data.uploadEachLevel = filtuploadEachLevel;

        return data;
    }

    private void towerRefresh() {


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
                    Intent refreshList = new Intent(getApplicationContext(), TowerActivity.class);
                    Bundle extras = new Bundle();

                    extras.putInt("com.example.speedtester.buildingIndex",data.buildingIndex);
                    extras.putString("com.example.speedtester.data", String.valueOf(APlist));

                    refreshList.putExtras(extras);
                    refreshList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    finish();// end the current activity
                    overridePendingTransition(0,0);
                    startActivity(refreshList);//start the activity again
                    refreshLayout.setRefreshing(false);
//                    try {
//                        data= getNumAp(buildingIndex,APlist,towerNames);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    data = filterAP(towerLevels,data.numAPEachLevel,data.normalEachLevel,data.warningEachLevel, data.criticalEachLevel,data.downloadEachLevel,data.uploadEachLevel);
//
//                    // Making variables global
//                    data.APListStrData = APlist.toString();
//                    data.buildingIndex = buildingIndex;
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Log.d("data","received" );
//                            towerlevelAdapter.towerRefresh(data.towerLevels,data.numAPEachLevel,data.normalEachLevel,data.warningEachLevel,data.criticalEachLevel,data.downloadEachLevel,data.uploadEachLevel);
//
//                        }
//                    });
                }
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
        String[] towerLevels;

        public static ArrayList<Integer> arrayIndex;
        public static String APListStrData;
        public static int buildingIndex;


    }


}
