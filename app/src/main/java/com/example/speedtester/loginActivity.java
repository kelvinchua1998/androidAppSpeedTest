package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginActivity extends AppCompatActivity {

    EditText phoneNumberEditText;
    EditText passwordEditText;
    Button loginButton;
    Button loginOTPButton;
    TextView forgetpwTextView;
    TextView registerTextView;
    TextView phoneNumbervalidatorTextView;
    TextView passwordvalidatorTextView;
    String phoneNumber;
    String password;
    GlobalApplication.UserDetails user = GlobalApplication.getuserdetails();

    GlobalApplication.Config config = GlobalApplication.getconfiq();


    final LoadingDialogue loadingDialogue = new LoadingDialogue(loginActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//check whether the user is already logged in
        Boolean islogged = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isLogged", false);
// if the user is logged in it will go to the building activity
        if(islogged){
            startActivity(new Intent(loginActivity.this, BuildingActivity.class));
            Toast.makeText(loginActivity.this, "user already logged in", Toast.LENGTH_LONG)
                    .show();
        }
//check whether the app for first run
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
//if the app is run for the first time, it will go to the activation page
        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(loginActivity.this, activationActivity.class));
            Toast.makeText(loginActivity.this, "Run only once", Toast.LENGTH_LONG)
                    .show();
        }

        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        phoneNumbervalidatorTextView = (TextView)findViewById(R.id.phonenumbervalidatorTextView);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordvalidatorTextView = (TextView)findViewById(R.id.passwordvalidatorTextView);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginOTPButton = (Button) findViewById(R.id.loginOTPButton);
        forgetpwTextView= (TextView) findViewById(R.id.forgetPasswordTextView);
        registerTextView = (TextView) findViewById(R.id.registerTextView);






        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if(validation(phoneNumber,password)){
                    loginMobile(phoneNumber,password);
                }

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
    private boolean validation(String phoneNumber, String password){
        boolean phonenumbervalidation=false, passwordvalidation=false;
        if(phoneNumber.length() != 8){

            phoneNumbervalidatorTextView.setText("please input a valid phone number!");
            phonenumbervalidation =false;
        }else{
            phoneNumbervalidatorTextView.clearComposingText();
            phonenumbervalidation =true;
        }
        if(password.length() == 0){

            passwordvalidatorTextView.setText("please enter password");
            passwordvalidation = false;
        }else{
            passwordvalidatorTextView.clearComposingText();
            passwordvalidation =true;
        }
        if(phonenumbervalidation == true && passwordvalidation == true)
            return true;
        else{
            return false;
        }
    }

    private void loginMobile(final String phoneNumber, final String password){
//start loading dialogue
        loadingDialogue.startLoadingDialogue();


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JSONObject bodyoptions = new JSONObject();

        try {
            bodyoptions.put("token", config.token);
            bodyoptions.put("type", "mobile");
            bodyoptions.put("password",password);
            bodyoptions.put("mobile",phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String options = bodyoptions.toString();
        RequestBody body = RequestBody.create(mediaType, options);
        Request request = new Request.Builder()
                .url(config.loginURL)
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

                    JSONObject jsonresponse;
                    try {
                        jsonresponse = new JSONObject(myresponse);
                        if(jsonresponse.getInt("errorCode") == 0){
                            loadingDialogue.dismissDialog();

                            Intent loginSuccessful = new Intent(loginActivity.this, BuildingActivity.class);

                            user.phonenumber = phoneNumber;
                            user.userToken = jsonresponse.getJSONObject("data").getString("token");

                            startActivity(loginSuccessful);
                            finish();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogged", true).apply();
                        }
                        else if(jsonresponse.getInt("errorCode") == 6){
                            loadingDialogue.dismissDialog();
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    passwordvalidatorTextView.setText("wrong password");
                                    passwordEditText.selectAll();
                                }
                            });


                        }
                        else if(jsonresponse.getInt("errorCode") == 5){
                            loadingDialogue.dismissDialog();
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    phoneNumbervalidatorTextView.setText("incorrect phone number");
                                    phoneNumberEditText.selectAll();
                                }
                            });


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            }

        });
    }
}
