package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    EditText phoneNumberEditText;
    EditText passwordEditText;
    Button loginButton;
    Button loginOTPButton;
    TextView forgetpwTextView;
    TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(loginActivity.this, activationActivity.class));
            Toast.makeText(loginActivity.this, "Run only once", Toast.LENGTH_LONG)
                    .show();
        }

        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginOTPButton = (Button) findViewById(R.id.loginOTPButton);
        forgetpwTextView= (TextView) findViewById(R.id.forgetPasswordTextView);
        registerTextView = (TextView) findViewById(R.id.registerTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginsuccessfull = new Intent(loginActivity.this, BuildingActivity.class);
                startActivity(loginsuccessfull);
            }
        });

        loginOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginOTP = new Intent(loginActivity.this, phoneOTPactivity.class);
                startActivity(loginOTP);
            }
        });
//        forgetpwTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent forgotPW = new Intent(loginActivity.this, phoneOTPactivity.class);
//                startActivity(loginOTP);
//            }
//        });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerNewUser = new Intent(loginActivity.this, registerActivity.class);
                startActivity(registerNewUser);
            }
        });




    }
}
