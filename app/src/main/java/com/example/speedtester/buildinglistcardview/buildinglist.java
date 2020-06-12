package com.example.speedtester.buildinglistcardview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.speedtester.GlobalApplication;
import com.example.speedtester.LoadingDialogue;
import com.example.speedtester.R;

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

public class buildinglist extends AppCompatActivity implements BuildinglistAdapter.RecyclerViewClickListener {

    BuildinglistAdapter.RecyclerViewClickListener listener;

    RecyclerView buildingRecyclerView;
    GlobalApplication.Config config = GlobalApplication.getconfiq();
    final LoadingDialogue loadingDialogue = new LoadingDialogue(buildinglist.this);
    GlobalApplication.Data shareddata = GlobalApplication.getData();
    Context context = GlobalApplication.getAppContext();
    ArrayList<building> buildingArrayList = new ArrayList<>();
    String[] buildingName;
    BuildinglistAdapter buildinglistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildinglist);

        buildingRecyclerView = (RecyclerView) findViewById(R.id.buildinglistrecyclerview);


         buildinglistAdapter = new BuildinglistAdapter(this, buildingArrayList , listener);
        buildingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        buildingRecyclerView.setAdapter(buildinglistAdapter);
        Resources res = getResources();
        buildingName = res.getStringArray(R.array.building_name);

        getAPlist();

    }

    @Override
    public void onClick(int position) {

    }

    class building{
        String name;
        int numberofAP=0;
        int warningAP=0;
        int criticalAP=0;
        int downloadAP=0;
        int uploadAP=0;
    }

    private void getAPlist(){

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

                loadingDialogue.dismissDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    Log.d("response test", "WORKED");
                    Log.d("response test", myresponse);


                    JSONArray APlist = null;
                    try {
                        JSONObject jsonData = new JSONObject(myresponse);
                        APlist = jsonData.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    shareddata.APlist = APlist.toString();

                    buildingArrayList = processData(APlist, buildingName);



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buildinglistAdapter.setbuildinglistAP(buildingArrayList);
                        }
                    });


                }
            }

        });
    }

    private ArrayList<building> processData(JSONArray aPlist, String[] buildingname) {

        building mainBuilding = new building();
        building podiumBuilding = new building();

        mainBuilding.name = buildingname[0];
        podiumBuilding.name = buildingname[1];

        for(int i= 0; i< aPlist.length(); i++){


            try {
                JSONObject singleap = (JSONObject) aPlist.get(i);
                if(singleap.getJSONObject("location").getString("building").equals(mainBuilding.name)){
                    mainBuilding.numberofAP++;
                    mainBuilding.uploadAP += singleap.getJSONObject("last_speedtest").getInt("upload");
                    mainBuilding.downloadAP += singleap.getJSONObject("last_speedtest").getInt("download");
                    switch(singleap.getInt("status")){
                        case 0:{

                            break;
                        }
                        case 1:{
                            mainBuilding.warningAP++;
                            break;
                        }
                        case 2:{
                            mainBuilding.criticalAP++;
                            break;
                        }
                    }
                }else if(singleap.getJSONObject("location").getString("building").equals(podiumBuilding.name)){
                    podiumBuilding.numberofAP++;
                    podiumBuilding.uploadAP += singleap.getJSONObject("last_speedtest").getInt("upload");
                    podiumBuilding.downloadAP += singleap.getJSONObject("last_speedtest").getInt("download");
                    switch(singleap.getInt("status")){
                        case 0:{

                            break;
                        }
                        case 1:{
                            podiumBuilding.warningAP++;
                            break;
                        }
                        case 2:{
                            podiumBuilding.criticalAP++;
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mainBuilding.downloadAP /= mainBuilding.numberofAP;
        podiumBuilding.downloadAP /= podiumBuilding.numberofAP;

        ArrayList<building> buildingArrayList = new ArrayList<>();

        buildingArrayList.add(mainBuilding);
        buildingArrayList.add(podiumBuilding);

        return buildingArrayList;
    }
}
