package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

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


public class verifyActivity extends AppCompatActivity {

    PinView pinview;
    Button verifyButton;
    TextView otpvalidator;
    GlobalApplication.Config config = GlobalApplication.getconfiq();
    final LoadingDialogue loadingDialogue = new LoadingDialogue(verifyActivity.this);

    GlobalApplication.UserDetails user = GlobalApplication.getuserdetails();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);


        Intent in = getIntent();

//        String usertoken = in.getStringExtra("usertoken");
        Bundle bundle = in.getExtras();

        final String phoneNumber = bundle.getString("phoneNumber");


        pinview = (PinView) findViewById(R.id.pinView);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        otpvalidator=(TextView) findViewById(R.id.otpvalidatorTextView);

        pinview.selectAll();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strOTP = pinview.getText().toString();

                if (validation(strOTP)){
                    verifyotp(phoneNumber,strOTP);
                }
            }
        });
    }
    private boolean validation(String strOTP){

        if(strOTP.length() != 6){
            pinview.setLineColor(Color.RED);
            return false;
        }else{
            pinview.setLineColor(Color.GREEN);
            return true;
        }


    }
    private void verifyotp(final String phoneNumber,String strOTP){


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JSONObject bodyoptions = new JSONObject();

        try {
            bodyoptions.put("token", config.token);
            bodyoptions.put("type", "2fa");
            bodyoptions.put("otp", strOTP);
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
                            Intent verifysuccessful = new Intent(verifyActivity.this, BuildingActivity.class);

                            user.phonenumber = phoneNumber;
                            user.userToken = jsonresponse.getJSONObject("data").getString("token");


                            startActivity(verifysuccessful);
                            finish();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogged", true).commit();
                        }
                        else if(jsonresponse.getInt("errorCode") == 13){
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    pinview.selectAll();
                                    otpvalidator.setText("incorrect OTP");

                                }
                            });
                        }
                        else if(jsonresponse.getInt("errorCode") == 12){
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    //change to dialogue
                                    otpvalidator.setText("Session ended. Please retry again");

                                    Intent sessionended = new Intent(verifyActivity.this, phoneOTPactivity.class);
                                    startActivity(sessionended);

                                    finish();
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
