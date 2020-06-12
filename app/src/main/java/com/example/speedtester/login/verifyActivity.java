package com.example.speedtester.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.speedtester.building_list.BuildingActivity;
import com.example.speedtester.GlobalApplication;
import com.example.speedtester.LoadingDialogue;
import com.example.speedtester.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    boolean validation=false;
    GlobalApplication.Config config = GlobalApplication.getconfiq();
    final LoadingDialogue loadingDialogue = new LoadingDialogue(verifyActivity.this);
    boolean isShowOTP = GlobalApplication.isDisplayOTP();
    GlobalApplication.UserDetails user = GlobalApplication.getuserdetails();
    TextView countdownandresendTextView;
    CountDownTimer countDownTimer1;
    CountDownTimer countDownTimer2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);


        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        final String phoneNumber = bundle.getString("phoneNumber");

        String otp = null;
        if(isShowOTP){
            otp = bundle.getString("otp");
        }


        pinview = (PinView) findViewById(R.id.pinView);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        otpvalidator=(TextView) findViewById(R.id.otpvalidatorTextView);
        countdownandresendTextView = (TextView)findViewById(R.id.countdownandresendbuttonTextView);

        if(isShowOTP)
            otpvalidator.setText("For Testing Purposes OTP: "+otp);

        pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 6){
                    pinview.setLineColor(Color.RED);
                    validation = false;
                }
                else{
                    pinview.setLineColor(Color.GREEN);
                    validation = true;
                }
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strOTP = pinview.getText().toString();

                if (validation){
                    verifyotp(phoneNumber,strOTP);
                }
            }
        });

        resend(phoneNumber);

    }

    private void resend(final String phoneNumber) {
        countDownTimer1 = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownandresendTextView.setText((millisUntilFinished/1000)+" sec");
            }

            @Override
            public void onFinish() {
                countdownandresendTextView.setText("Resend OTP");
                countdownandresendTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        resendOTP(phoneNumber);
                        countdownandresendTextView.setText("An OTP has been sent!");

                        countDownTimer2 = new CountDownTimer(5000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                countDownTimer1.start();
                            }
                        }.start();

                    }
                });
            }
        }.start();
    }

    private void resendOTP(String phoneNumber) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JSONObject bodyoptions = new JSONObject();

        try {
            bodyoptions.put("token", config.token);
            bodyoptions.put("type", "getotp");
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

                            final String otp = jsonresponse.getJSONObject("data").getString("verifyCode");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    otpvalidator.setText("For Testing Purposes OTP: "+otp);

                                }
                            });
                        }
//                        else if(jsonresponse.getInt("errorCode") == 9){
//                            runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//
//                                    // Stuff that updates the UI
//                                    phoneValidatorTextView.setText("Phone not registered. Please register");
//
//                                }
//                            });
//
//
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            }

        });
    }


    //    private boolean validation(String strOTP){
//
//        if(strOTP.length() != 6){
//            pinview.setLineColor(Color.RED);
//            return false;
//        }else{
//            pinview.setLineColor(Color.GREEN);
//            return true;
//        }
//    }
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
