package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;



public class verifyActivity extends AppCompatActivity {

    PinView pinview;
    Button verifyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);


        pinview = (PinView) findViewById(R.id.pinView);
        verifyButton = (Button) findViewById(R.id.verifyButton);



        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String OTP = pinview.getText().toString();

                Intent otpVerify = new Intent(verifyActivity.this, BuildingActivity.class);

                if (OTP.equals("00000"))
                    startActivity(otpVerify);
                else
                    pinview.setLineColor(Color.RED);
            }
        });
    }
}
