package com.example.speedtester;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ListView BuildingListView;
    String[] buildingName;//towera
    int buildingLevelA ,buildingLevelB;
    JSONObject jsonData ;
    JSONArray APlist ;
    buildingAdapter BuildingAdapter;
    int i,AnumAP =0 , BnumAP=0;

    int[] buildingnumAP ;
    int[] buildingLevel;//total number of levels

//    Todo
//
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

        //putting the total num of levels into an array
        buildingLevelA = (res.getStringArray(R.array.tower_a_levels)).length;
        buildingLevelB = (res.getStringArray(R.array.tower_b_levels)).length;
        buildingLevel = new int[]{buildingLevelA, buildingLevelB};

        BuildingAdapter = new buildingAdapter(this, buildingName, buildingLevel,buildingnumAP);
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
    private void connectAPI(){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "token=ectivisecloudDBAuthCode:b84846daf467cede0ee462d04bcd0ade");
        Request request = new Request.Builder()
                .url("http://dev1.ectivisecloud.com:8081/api/speedtest/getaplist")
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
                                if(location.get("building").toString().equals("Tower A") )
                                    AnumAP++;
                                else if (location.get("building").toString().equals("Tower B"))
                                    BnumAP++;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    buildingnumAP = new int[]{AnumAP,BnumAP};
                    Log.d("data","AnumAP: "+ AnumAP+" BnumAP: "+BnumAP);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            buildingnumAP = new int[]{AnumAP,BnumAP};
                            Log.d("data","received" );
                            BuildingListView.setAdapter(BuildingAdapter);
                            BuildingAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }

        });
    }
}
