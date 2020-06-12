package com.example.speedtester.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.speedtester.R;

public class activationActivity extends AppCompatActivity {

    EditText ipAddressEditText;
    EditText portNumEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);




        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        ipAddressEditText = (EditText) findViewById(R.id.ipAddressActivationEditText);
        portNumEditText = (EditText) findViewById(R.id.portActivationEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumbertActivationEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordActivationEditText);

        nextButton = (Button) findViewById(R.id.nextActivationButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activateUser = new Intent(activationActivity.this, loginActivity.class);
                startActivity(activateUser);
            }
        });

    }
}
