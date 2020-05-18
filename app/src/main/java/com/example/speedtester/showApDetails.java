package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class showApDetails extends AppCompatActivity {
    String ssid, location, wifiController, site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ap_details);

        Bundle bundle = getIntent().getExtras();

        String ssidIn = bundle.getString("com.example.speedtester.ssid");
        String siteIn = bundle.getString("com.example.speedtester.site");
        String locationIn = bundle.getString("com.example.speedtester.location");
        String IpIn = bundle.getString("com.example.speedtester.ip");

        TextView ssidTextView, siteTextView, locationTextView,ipTextView;

        ssidTextView = (TextView)findViewById(R.id.showSsidTextView);
        siteTextView = (TextView)findViewById(R.id.showSiteTextView);
        locationTextView = (TextView)findViewById(R.id.showLocationTextView);
        ipTextView = (TextView)findViewById(R.id.showIpTextView);

        ssidTextView.setText("SSID: "+ssidIn);
        siteTextView.setText("Site: "+siteIn);
        locationTextView.setText("Location: "+locationIn);
        ipTextView.setText("IP: "+IpIn);

    }
}
