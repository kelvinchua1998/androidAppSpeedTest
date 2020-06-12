package com.example.speedtester;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class showApDetails extends AppCompatActivity {
    JSONArray APlist;
    JSONObject singleAP;
    SwipeRefreshLayout refreshLayout;
    String ssid,password,os,hardware,mac,raspi,description, site,building,level , ip, name;
    int ping;
    int download;
    int upload;
    int jitter;
    int runtime;
    int status;
    int device_id;
    int ignore;
    int quality;
    long timestamp;
    GraphView graphView;
    JSONArray testresultlist;
    int deviceID;
    GlobalApplication.Config config = GlobalApplication.getconfiq();
    GlobalApplication.Data shareddata = GlobalApplication.getData();
    Context context = GlobalApplication.getAppContext();
    Intentinglobal intentinglobal = new Intentinglobal();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ap_details);

        Bundle bundle = getIntent().getExtras();

        deviceID  = bundle.getInt("com.example.speedtester.device_id");
        String APListStrData = bundle.getString("com.example.speedtester.data");

        intentinglobal.device_id = deviceID;
        try {
            APlist = new JSONArray(APListStrData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView divider1,divider2;
        TextView ssidTextView, ipTextView,passwordTextView, runtimeTextView, device_idTextView,osTextView, hardwareTextView, ignoreTextView;
        TextView statusTextView, qualityTextView,macTextView, raspiTextView, siteTextView,buildingTextView, levelTextView;
        TextView pingTextView, downloadTextView,uploadTextView, jitterTextView, timestampTextView,descriptionTextView;

//        divider1 = (ImageView) findViewById(R.id.divider1ImageView);
//        divider2 = (ImageView) findViewById(R.id.divider2ImageView);

//        ssidTextView = (TextView)findViewById(R.id.showSsidTextView);

//        ipTextView = (TextView)findViewById(R.id.showIpTextView);
//        passwordTextView = (TextView)findViewById(R.id.passwordTextView);
//        runtimeTextView = (TextView)findViewById(R.id.runtimessidTextView);
//        device_idTextView = (TextView)findViewById(R.id.device_idTextView);
//        osTextView = (TextView)findViewById(R.id.osTextView);
//        hardwareTextView = (TextView)findViewById(R.id.hardwareTextView);
//        ignoreTextView = (TextView)findViewById(R.id.ignoreTextView);
//        statusTextView = (TextView)findViewById(R.id.statusAPTextView);
//        qualityTextView = (TextView)findViewById(R.id.qualityTextView);
//        macTextView = (TextView)findViewById(R.id.macTextView);
//        raspiTextView = (TextView)findViewById(R.id.raspiTextView);

//        siteTextView = (TextView)findViewById(R.id.showSiteTextView);
        buildingTextView = (TextView)findViewById(R.id.BuildingTextView);
        levelTextView = (TextView)findViewById(R.id.levelAPTextView);

        pingTextView = (TextView)findViewById(R.id.pingTextView);
        downloadTextView = (TextView)findViewById(R.id.downloadAPTextView);
        uploadTextView = (TextView)findViewById(R.id.uploadAPTextView);
        jitterTextView = (TextView)findViewById(R.id.jitterTextView);
        timestampTextView = (TextView)findViewById(R.id.timestampTextView);

//        descriptionTextView = (TextView)findViewById(R.id.descTextView);



        try {
            for (int j = 0; j<APlist.length();j++){
                singleAP = APlist.getJSONObject(j);
                if (singleAP.getInt("device_id")==deviceID)
                    break;
            }
//            singleAP = APlist.getJSONObject(APIndex);
            name = singleAP.getString("name");
//            ssid = singleAP.getString("ssid");
//            ip = singleAP.getString("ip");
//            password = singleAP.getString("password");
//            hardware = singleAP.getString("hardware");
//            mac = singleAP.getString("mac");
//            raspi = singleAP.getString("raspi");
//            description = singleAP.getString("desc");
//            os = singleAP.getString("os");

//            site = singleAP.getJSONObject("location").getString("site");
            building = singleAP.getJSONObject("location").getString("building");
            level = singleAP.getJSONObject("location").getString("level");

            ping = singleAP.getJSONObject("last_speedtest").getInt("ping");
            download = singleAP.getJSONObject("last_speedtest").getInt("download");
            upload = singleAP.getJSONObject("last_speedtest").getInt("upload");
            jitter = singleAP.getJSONObject("last_speedtest").getInt("jitter");
            timestamp = singleAP.getJSONObject("last_speedtest").getLong("timestamp");

//            ignore = singleAP.getInt("ignore");
            status = singleAP.getInt("status");
//            quality = singleAP.getInt("quality");
//            runtime = singleAP.getInt("runtime");
//            device_id = singleAP.getInt("device_id");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        getSupportActionBar().setTitle(name);
//        ssidTextView.setText(ssid);
//        ipTextView.setText("ip: "+ip);
//        passwordTextView.setText("password: "+password);
        String convertedtime = convertRuntime(runtime);
//        runtimeTextView.setText(convertedtime);
//        device_idTextView.setText("device_id: "+device_id);
//        osTextView.setText("os: "+os);
//        hardwareTextView.setText("hardware: "+hardware);
//        ignoreTextView.setText("ignore: "+ignore);

//        SpannableString status = setStatusTextView();
//        statusTextView.setText(status);

//        qualityTextView.setText("quality: "+quality);
//        macTextView.setText("mac: "+mac);
//        raspiTextView.setText("raspi: "+raspi);

//        siteTextView.setText("Site: "+site);
        buildingTextView.setText(building);
        if(Integer.valueOf(level) == -1)
            levelTextView.setText("B1");
        else
            levelTextView.setText(level);

        pingTextView.setText(ping+" ms");
        downloadTextView.setText(download+" Mb/s");
        uploadTextView.setText(upload+" Mb/s");
        jitterTextView.setText(jitter+" ms");

//        Timestamp ts=new Timestamp(timestamp);
//        Date date = ts;

        Date date=new Date(timestamp);

        String pattern = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern);
//
        timestampTextView.setText(simpleDateFormat.format(date));

        Drawable mDivider = ContextCompat.getDrawable(this, R.drawable.divider);
//        divider1.setImageDrawable(mDivider);
//        divider2.setImageDrawable(mDivider);


//        descriptionTextView.setText("description: "+description);

        gettestresultlist(name);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.showAPRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                APRefresh();
            }
        });

    }

    private Lastspeedtest proccessdata(JSONArray testresultlist) {
        DataPoint[] downloadDatapoints = new DataPoint[30];
        DataPoint[] uploadDatapoints = new DataPoint[30];
        DataPoint[] pingDatapoints = new DataPoint[30];
        DataPoint[] jitterDatapoints = new DataPoint[30];
        Lastspeedtest lastspeedtest = new Lastspeedtest();
        for (int i = 0; i<30;i++){


            try {
                JSONObject singletestresult = testresultlist.getJSONObject(i);
                Date date = new Date(singletestresult.getLong("timestamp"));
                int download = singletestresult.getInt("download");
                int upload = singletestresult.getInt("upload");
                int ping = singletestresult.getInt("ping");
                int jitter = singletestresult.getInt("jitter");

                downloadDatapoints[29-i]=new DataPoint(date,download);
                uploadDatapoints[29-i]=new DataPoint(date,upload);
                pingDatapoints[29-i]=new DataPoint(date,ping);
                jitterDatapoints[29-i]=new DataPoint(date,jitter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Graphparams download = new Graphparams();
        Graphparams upload = new Graphparams();
        Graphparams ping = new Graphparams();
        Graphparams jitter = new Graphparams();

        download.datapoints=downloadDatapoints;
        download.title = "Download speed past 1hour";
        download.verticalaxistitle = "Download speed /Mps";
        download.horizontalaxistitle = "time";

        upload.datapoints=uploadDatapoints;
        upload.title = "upload speed past 1hour";
        upload.verticalaxistitle = "upload speed /Mps";
        upload.horizontalaxistitle = "time";

        ping.datapoints=pingDatapoints;
        ping.title = "ping past 1hour";
        ping.verticalaxistitle = "ping /ms";
        ping.horizontalaxistitle = "time";

        jitter.datapoints=jitterDatapoints;
        jitter.title = "jitter past 1hour";
        jitter.verticalaxistitle = "jitter /ms";
        jitter.horizontalaxistitle = "time";


        lastspeedtest.downloadgraphparams =download;
        lastspeedtest.uploadgraphparams =upload;
        lastspeedtest.pinggraphparams =ping;
        lastspeedtest.jittergraphparams =jitter;

        return lastspeedtest;
    }

    private void gettestresultlist(String name) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject bodyoptions = new JSONObject();

        try {
            bodyoptions.put("token", config.token);
            bodyoptions.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String options = bodyoptions.toString();
        RequestBody body = RequestBody.create(mediaType, options);
        Request request = new Request.Builder()
                .url(config.gettestresult)
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
                        JSONObject jsonData = new JSONObject(myresponse);
                        testresultlist = jsonData.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GraphView downloadgraph = (GraphView) findViewById(R.id.downloadgraph);
                            GraphView uploadgraph = (GraphView) findViewById(R.id.uploadgraph);
                            GraphView pinggraph = (GraphView) findViewById(R.id.pinggraph);
                            GraphView jittergraph = (GraphView) findViewById(R.id.jittergraph);


                            Lastspeedtest lastspeedtest = proccessdata(testresultlist);

                            plotgraph(lastspeedtest.downloadgraphparams,downloadgraph);
                            plotgraph(lastspeedtest.uploadgraphparams,uploadgraph);
                            plotgraph(lastspeedtest.pinggraphparams,pinggraph);
                            plotgraph(lastspeedtest.jittergraphparams,jittergraph);


                        }
                    });

                }
            }

        });
    }

    private void plotgraph(Graphparams graphparams,GraphView graphView) {


        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(graphparams.datapoints);
        graphView.setTitle(graphparams.title);
        graphView.setTitleTextSize(50);
//        graph.getLegendRenderer().setVisible(true);

        String pattern1 = "HH:mm";
        DateFormat DateFormat = new SimpleDateFormat(pattern1);


//                            graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        graphView.getViewport().setMinX(graphparams.datapoints[0].getX());
        graphView.getViewport().setMaxX(graphparams.datapoints[29].getX());
        graphView.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
//                            graph.getGridLabelRenderer().setHumanRounding(false);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setVerticalAxisTitle(graphparams.verticalaxistitle);
        gridLabelRenderer.setHorizontalAxisTitle(graphparams.horizontalaxistitle);
        gridLabelRenderer.setHorizontalLabelsAngle(12);
        gridLabelRenderer.setLabelFormatter(new DateAsXAxisLabelFormatter(context, DateFormat));
//
//                            String pattern1 = "HH:mm";
//                            DateFormat DateFormat = new SimpleDateFormat(pattern1);
//
//        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//        staticLabelsFormatter.setHorizontalLabels(new String[] {DateFormat.format(datapoints[0].getX()),"idk", DateFormat.format(datapoints[29].getX())});
////        staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
//        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphView.addSeries(series);

    }

    public class Lastspeedtest{
        Graphparams downloadgraphparams;
        Graphparams uploadgraphparams;
        Graphparams pinggraphparams;
        Graphparams jittergraphparams;
    }

    public class Graphparams{
        DataPoint[] datapoints;
        String title;
        String horizontalaxistitle;
        String verticalaxistitle;
    }

    private void APRefresh() {

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

                    Intent refreshAPDetail = new Intent(getApplicationContext(),showApDetails.class);

                    Bundle extras = new Bundle();

                    extras.putInt("com.example.speedtester.device_id", intentinglobal.device_id);
                    extras.putString("com.example.speedtester.data", APlist.toString());

                    refreshAPDetail.putExtras(extras);

                    refreshAPDetail.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//remove animation
                    finish();// end the current activity
                    overridePendingTransition(0,0);//remove animation

                    startActivity(refreshAPDetail);

                    refreshLayout.setRefreshing(false);
//
                }
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String convertRuntime(int timestamp) {
        int days = Math.floorDiv(timestamp, 3600*24);
        timestamp -= days * (3600*24);
        int hours = Math.floorDiv(timestamp, 3600);

        String convertedtime = days + " days " + hours +" hours";

        return convertedtime;
    }

    private SpannableString setStatusTextView() {
        if(status==0){
            String text = "Status: Normal";
            SpannableString ss = new SpannableString(text);
            ForegroundColorSpan greenIndicator = new ForegroundColorSpan(getResources().getColor(R.color.indicatorGreen));
            ss.setSpan(greenIndicator, 8,14, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            return ss;
        }

        else if (status==1) {
            String text = "Status: Warning";
            SpannableString ss = new SpannableString(text);
            ForegroundColorSpan greenIndicator = new ForegroundColorSpan(getResources().getColor(R.color.indicatorOrange));
            ss.setSpan(greenIndicator, 8,15, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            return ss;
        }

        else if (status==2) {
            String text = "Status: Critical";
            SpannableString ss = new SpannableString(text);
            ForegroundColorSpan greenIndicator = new ForegroundColorSpan(getResources().getColor(R.color.indicatorRed));
            ss.setSpan(greenIndicator, 8,16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            return ss;
        }
        return null;
    }

    public class Intentinglobal{
        int device_id;
    }

}
