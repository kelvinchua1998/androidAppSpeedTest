package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class registerActivity extends AppCompatActivity {
    EditText phoneNumberEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerButton;
    TextView loginTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberRegisterEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordRegisterEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordRegisterEditText);

        registerButton = (Button) findViewById(R.id.registerButton);
        loginTextView = (TextView) findViewById(R.id.loginTextView);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginFromRegister = new Intent(registerActivity.this, loginActivity.class);
                startActivity(loginFromRegister);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registersuccessfull = new Intent(registerActivity.this, verifyActivity.class);
                startActivity(registersuccessfull);
            }
        });
    }
}
