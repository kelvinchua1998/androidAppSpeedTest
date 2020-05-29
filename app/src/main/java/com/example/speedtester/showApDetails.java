package com.example.speedtester;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class showApDetails extends AppCompatActivity {
    JSONArray APlist;
    JSONObject singleAP;

    String ssid,password,os,hardware,mac,raspi,description, site,building,level , ip;
    int ping,download,upload,jitter,runtime,status,device_id,ignore,quality,timestamp;
    GraphView graphView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ap_details);

        Bundle bundle = getIntent().getExtras();

        int APIndex  = bundle.getInt("com.example.speedtester.APIndex");
        String APListStrData = bundle.getString("com.example.speedtester.data");

        try {
            APlist = new JSONArray(APListStrData);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView ssidTextView, ipTextView,passwordTextView, runtimeTextView, device_idTextView,osTextView, hardwareTextView, ignoreTextView;
        TextView statusTextView, qualityTextView,macTextView, raspiTextView, siteTextView,buildingTextView, levelTextView;
        TextView pingTextView, downloadTextView,uploadTextView, jitterTextView, timestampTextView,descriptionTextView;

        ssidTextView = (TextView)findViewById(R.id.showSsidTextView);

//        ipTextView = (TextView)findViewById(R.id.showIpTextView);
//        passwordTextView = (TextView)findViewById(R.id.passwordTextView);
        runtimeTextView = (TextView)findViewById(R.id.runtimessidTextView);
//        device_idTextView = (TextView)findViewById(R.id.device_idTextView);
//        osTextView = (TextView)findViewById(R.id.osTextView);
//        hardwareTextView = (TextView)findViewById(R.id.hardwareTextView);
//        ignoreTextView = (TextView)findViewById(R.id.ignoreTextView);
        statusTextView = (TextView)findViewById(R.id.statusAPTextView);
//        qualityTextView = (TextView)findViewById(R.id.qualityTextView);
//        macTextView = (TextView)findViewById(R.id.macTextView);
//        raspiTextView = (TextView)findViewById(R.id.raspiTextView);

        siteTextView = (TextView)findViewById(R.id.showSiteTextView);
        buildingTextView = (TextView)findViewById(R.id.BuildingTextView);
        levelTextView = (TextView)findViewById(R.id.levelAPTextView);

        pingTextView = (TextView)findViewById(R.id.pingTextView);
        downloadTextView = (TextView)findViewById(R.id.downloadAPTextView);
        uploadTextView = (TextView)findViewById(R.id.uploadAPTextView);
        jitterTextView = (TextView)findViewById(R.id.jitterTextView);
        timestampTextView = (TextView)findViewById(R.id.timestampTextView);

//        descriptionTextView = (TextView)findViewById(R.id.descTextView);



        try {
            singleAP = APlist.getJSONObject(APIndex);

            ssid = singleAP.getString("ssid");
            ip = singleAP.getString("ip");
            password = singleAP.getString("password");
            hardware = singleAP.getString("hardware");
            mac = singleAP.getString("mac");
            raspi = singleAP.getString("raspi");
            description = singleAP.getString("desc");
            os = singleAP.getString("os");

            site = singleAP.getJSONObject("location").getString("site");
            building = singleAP.getJSONObject("location").getString("building");
            level = singleAP.getJSONObject("location").getString("level");

            ping = singleAP.getJSONObject("last_speedtest").getInt("ping");
            download = singleAP.getJSONObject("last_speedtest").getInt("download");
            upload = singleAP.getJSONObject("last_speedtest").getInt("upload");
            jitter = singleAP.getJSONObject("last_speedtest").getInt("jitter");
            timestamp = singleAP.getJSONObject("last_speedtest").getInt("timestamp");

            ignore = singleAP.getInt("ignore");
            status = singleAP.getInt("status");
            quality = singleAP.getInt("quality");
            runtime = singleAP.getInt("runtime");
            device_id = singleAP.getInt("device_id");


        } catch (JSONException e) {
            e.printStackTrace();
        }



        ssidTextView.setText(ssid);
//        ipTextView.setText("ip: "+ip);
//        passwordTextView.setText("password: "+password);
        String convertedtime = convertRuntime(runtime);
        runtimeTextView.setText("runtime: "+ convertedtime);
//        device_idTextView.setText("device_id: "+device_id);
//        osTextView.setText("os: "+os);
//        hardwareTextView.setText("hardware: "+hardware);
//        ignoreTextView.setText("ignore: "+ignore);

        SpannableString status = setStatusTextView();
        statusTextView.setText(status);

//        qualityTextView.setText("quality: "+quality);
//        macTextView.setText("mac: "+mac);
//        raspiTextView.setText("raspi: "+raspi);

        siteTextView.setText("Site: "+site);
        buildingTextView.setText("Building: "+building);
        levelTextView.setText("Level: "+level);

        pingTextView.setText("Ping: "+ping+"ms");
        downloadTextView.setText("Download: "+download+"Mb/s");
        uploadTextView.setText("Upload: "+upload+"Mb/s");
        jitterTextView.setText("Jitter: "+jitter);

        timestampTextView.setText("Timestamp: "+timestamp);

//        descriptionTextView.setText("description: "+description);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

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


}
