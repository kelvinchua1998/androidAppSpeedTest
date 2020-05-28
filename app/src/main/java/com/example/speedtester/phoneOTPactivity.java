package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class phoneOTPactivity extends AppCompatActivity {

    EditText otpPhoneNumberEditText;
    Button otpNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_o_t_pactivity);

        otpPhoneNumberEditText = (EditText) findViewById(R.id.otpPhoneNumberEditText);
        otpNextButton = (Button) findViewById(R.id.otpNextButton);

        otpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otpNext = new Intent(phoneOTPactivity.this, verifyActivity.class);
                startActivity(otpNext);
            }
        });
    }
}
