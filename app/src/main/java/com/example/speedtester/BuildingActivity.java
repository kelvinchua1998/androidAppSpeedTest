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

import java.io.IOException;

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
    JSONObject jsonData ;
    JSONArray APlist ;
    buildingAdapter BuildingAdapter;
    int i,AnumAP =0 , BnumAP=0;
    String[] config;
    int[] buildingnumAP ;
    int[] buildingLevel;//total number of levels

//    Todo
//making the building names and levels dynamic, TBC
//     1) warning number
//
//    2) critical number


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectAPI();// connect api function

        Resources res = getResources();
        BuildingListView = (ListView) findViewById(R.id.buldingListView);
        buildingName = res.getStringArray(R.array.building_name);
        config = res.getStringArray(R.array.config);
        //putting the total num of levels into an array
        buildingLevelA = (res.getStringArray(R.array.Main_Block)).length;
        buildingLevelB = (res.getStringArray(R.array.Podium_Block)).length;
        buildingLevel = new int[]{buildingLevelA, buildingLevelB};

        BuildingAdapter = new buildingAdapter(this, buildingName, buildingLevel,buildingnumAP);
        BuildingListView.setAdapter(BuildingAdapter);


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
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "token=ectivisecloudDBAuthCode:b84846daf467cede0ee462d04bcd0ade");
        Request request = new Request.Builder()
                .url("http://192.168.1.124:8081/api/speedtest/getaplist")
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
                        Log.d("response test", "string to JSON");
                        Log.d("response test", APlist.toString());
                        for (i=0; i<APlist.length();i++){
                            JSONObject singleAP;

                                singleAP = APlist.getJSONObject(i);
                                JSONObject location = singleAP.getJSONObject("location");
                                if(location.get("building").toString().equals("Main Block") )
                                    AnumAP++;
                                else if (location.get("building").toString().equals("Podium Block"))
                                    BnumAP++;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("data","AnumAP: "+ AnumAP+" BnumAP: "+BnumAP);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            buildingnumAP = new int[]{AnumAP, BnumAP};
                            Log.d("data","received" );
                            BuildingAdapter.setbuildingAP(buildingnumAP);

                        }
                    });

                }
            }

        });
    }
}
