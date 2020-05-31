package com.example.speedtester;


import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
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

public class BuildingActivity extends AppCompatActivity {
    ListView BuildingListView;
    String[] buildingName;//towera
    int buildingLevelA ,buildingLevelB;
    APData data;
    JSONObject jsonData ;
    JSONArray APlist ;
    buildingAdapter BuildingAdapter;
    int i,AnumAP =0 , BnumAP=0;
    int[] warningList = {0,0}, criticalList = {0,0};
    int[] downloadList = {0,0}, uploadList = {0,0};
    String[] config;
    int[] buildingnumAP ;
    int[] buildingLevel;//total number of levels
    Context context = GlobalApplication.getAppContext();

    ArrayList<Integer>[] warningAPindex = new ArrayList[2];

    ArrayList<Integer>[] criticalAPindex = new ArrayList[2];

    SwipeRefreshLayout refreshLayout;


//    Todo
//making the building names and levels dynamic, TBC
//     1) warning number
//
//    2) critical number


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Loading dialogue

        final LoadingDialogue loadingDialogue = new LoadingDialogue(BuildingActivity.this);
        loadingDialogue.startLoadingDialogue();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialogue.dismissDialog();
            }
        },5000);

        for ( i = 0; i < warningAPindex.length ; i++) {
            warningAPindex[i] = new ArrayList<>();
        }
        for ( i = 0; i < criticalAPindex.length ; i++) {
            criticalAPindex[i] = new ArrayList<>();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Le Grove");

        connectAPI();

        Resources res = getResources();
        BuildingListView = (ListView) findViewById(R.id.buldingListView);

        buildingLevelA = (res.getStringArray(R.array.Main_Block)).length;
        buildingLevelB = (res.getStringArray(R.array.Podium_Block)).length;
        buildingName = res.getStringArray(R.array.building_name);
//        config = res.getStringArray(R.array.config);

        //putting the total num of levels into an array

        buildingLevel = new int[]{buildingLevelA, buildingLevelB};

        BuildingAdapter = new buildingAdapter(this, buildingName, buildingLevel,buildingnumAP,warningList,criticalList,downloadList,uploadList);
        BuildingListView.setAdapter(BuildingAdapter);

        //swipe refresh function
        refreshLayout = findViewById(R.id.buildingRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                connectAPI();
                refreshLayout.setRefreshing(false);
            }
        });


        BuildingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showTowerActivity = new Intent(getApplicationContext(), TowerActivity.class);
                Bundle extras = new Bundle();

                extras.putInt("com.example.speedtester.buildingIndex",position);
                extras.putString("com.example.speedtester.data", String.valueOf(APlist));

                showTowerActivity.putExtras(extras);
                startActivity(showTowerActivity);
            }
        });




    }
    private void connectAPI(){

        //initialise variables

        final int[] AnumAP = {0};
        final int[] BnumAP = {0};
        final int[] warningList = {0,0}, criticalList = {0,0};
        final int[] downloadList = {0,0}, uploadList = {0,0};

        boolean isTest = true;
        String url ;
        if(isTest) url = "http://192.168.1.124:8081/api/speedtest/getaplist";
        else  url = "http://dev1.ectivisecloud.com:8081/api/speedtest/getaplist";

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "token=ectivisecloudDBAuthCode:b84846daf467cede0ee462d04bcd0ade");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("response test", "FAILEED");
                Log.d("response test", e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    Log.d("response test", "WORKED");
                    Log.d("response test", myresponse);


                    try {
                        jsonData = new JSONObject(myresponse);
                        APlist = jsonData.getJSONArray("data");

                        //making it global
                        APData.APListStrData = APlist.toString();

                        Log.d("response test", "string to JSON");
                        Log.d("response test", APlist.toString());
                        for (int i=0; i<APlist.length();i++){
                            JSONObject singleAP;

                                singleAP = APlist.getJSONObject(i);
                                JSONObject location = singleAP.getJSONObject("location");
                                if(location.get("building").toString().equals("Main Block") )
                                {
                                    //get total number of AP for podium
                                    AnumAP[0]++;
                                    //get sum of download and upload for main building
                                    downloadList[0] += singleAP.getJSONObject("last_speedtest").getInt("download");
                                    uploadList[0] += singleAP.getJSONObject("last_speedtest").getInt("upload");

                                    //get sum of warning and critical AP
                                    if (singleAP.getInt("status")==1){
                                        warningAPindex[0].add(i);
                                        warningList[0]++;
                                    }

                                    else if (singleAP.getInt("status")==2){
                                        criticalAPindex[0].add(i);
                                        criticalList[0]++;
                                    }

                                }

                                else if (location.get("building").toString().equals("Podium Block"))
                                {
                                    //get total number of AP for podium
                                    BnumAP[0]++;
                                    //get sum of download and upload for podium building
                                    downloadList[1] += singleAP.getJSONObject("last_speedtest").getInt("download");
                                    uploadList[1] += singleAP.getJSONObject("last_speedtest").getInt("upload");
                                    //get sum of warning and critical AP
                                    if (singleAP.getInt("status")==1){
                                        warningAPindex[1].add(i);
                                        warningList[1]++;
                                    }

                                    else if (singleAP.getInt("status")==2){
                                        criticalAPindex[1].add(i);
                                        criticalList[1]++;
                                    }

                                }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.warningAPIndex = warningAPindex;
                    data.criticalAPIndex = criticalAPindex;

                    //get the mean for download and upload
                    downloadList[0] /= AnumAP[0];
                    uploadList[0] /= AnumAP[0];
                    downloadList[1] /= BnumAP[0];
                    uploadList[1] /= BnumAP[0];

                    Log.d("data","AnumAP: "+ AnumAP[0] +" BnumAP: "+ BnumAP[0]);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            buildingnumAP = new int[]{AnumAP[0], BnumAP[0]};

                            Log.d("data","received" );
                            BuildingAdapter.setbuildingAP(buildingnumAP, warningList, criticalList, downloadList, uploadList);

                        }
                    });

                }
            }

        });
    }


    public void onWarningClick(View v, int position) {
        Intent showWarningAP = new Intent(context, ssidActivity.class);

        Bundle extras = new Bundle();
        extras.putString("from", "buildingWarning");

//        int trueLevel = data.arrayIndex.get(position);
        extras.putIntegerArrayList("com.example.speedtester.APIndex",data.warningAPIndex[position]);

        extras.putString("com.example.speedtester.data", data.APListStrData);

//        extras.putInt("com.example.speedtester.level", trueLevel); //pass the postion to next screen
        extras.putInt("com.example.speedtester.buildingIndex", position);

        showWarningAP.putExtras(extras);
        v.getContext().startActivity(showWarningAP);
    }

    public void onCriticalClick(View v, int position) {
        Intent showWarningAP = new Intent(context, ssidActivity.class);

        Bundle extras = new Bundle();
        extras.putString("from", "buildingCritical");

//        int trueLevel = data.arrayIndex.get(position);
        extras.putIntegerArrayList("com.example.speedtester.APIndex",data.criticalAPIndex[position]);

        extras.putString("com.example.speedtester.data", data.APListStrData);

//        extras.putInt("com.example.speedtester.level", trueLevel); //pass the postion to next screen
        extras.putInt("com.example.speedtester.buildingIndex", position);

        showWarningAP.putExtras(extras);
        v.getContext().startActivity(showWarningAP);
    }
    public static class APData{
//        public static ArrayList<Integer>[] indexAPEachLevel;
        public static Context context;
//        int[] numAPEachLevel;
//        int[] warningEachLevel;
//        int[] criticalEachLevel;
//        int[] normalEachLevel;
//        int[] uploadEachLevel;
//        int[] downloadEachLevel;

        public static ArrayList<Integer> arrayIndex;
        public static String APListStrData;
        public static int buildingIndex;
        public static ArrayList[] warningAPIndex;
        public static ArrayList[] criticalAPIndex;

    }
}
