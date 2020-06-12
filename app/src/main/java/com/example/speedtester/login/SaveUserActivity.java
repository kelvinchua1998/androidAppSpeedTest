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
import android.widget.EditText;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.speedtester.GlobalApplication;
import com.example.speedtester.LoadingDialogue;
import com.example.speedtester.R;
import com.example.speedtester.building_list.BuildingActivity;

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

public class SaveUserActivity extends AppCompatActivity {

    PinView pinview;
    EditText passwordEditText;
    Button registerButton;
    TextView otpvalidator;
    TextView passwordvalidator;
    TextView coundownandresendotpTextView;
    GlobalApplication.Config config = GlobalApplication.getconfiq();
    boolean pinViewvalidation = false;
    boolean passwordvalidation = false;
    boolean isShowOTP = GlobalApplication.isDisplayOTP();
    GlobalApplication.UserDetails user = GlobalApplication.getuserdetails();
    final LoadingDialogue loadingDialogue = new LoadingDialogue(SaveUserActivity.this);

    CountDownTimer countDownTimer1;
    CountDownTimer countDownTimer2;
    String usertoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user);

        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        final String phoneNumber = bundle.getString("phoneNumber");
        usertoken = bundle.getString("usertoken");

        String otp = null;
        if(isShowOTP){
            otp = bundle.getString("otp");
        }


        pinview = (PinView) findViewById(R.id.pinViewsignup);
        passwordEditText =(EditText)findViewById(R.id.passwordsignupEditText);
        passwordvalidator= (TextView) findViewById(R.id.passwordvalidatorsignupTextView);
        otpvalidator=(TextView) findViewById(R.id.otpvalidatorsignupTextView);
        registerButton = (Button) findViewById(R.id.registerButton);
        coundownandresendotpTextView = (TextView) findViewById(R.id.countdownandresendbuttonsaveuserTextView);

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
                    pinViewvalidation = false;
                }
                else{
                    pinview.setLineColor(Color.GREEN);
                    pinViewvalidation = true;
                }
            }


        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    passwordvalidator.setText("please enter your password!");
                    passwordvalidation = false;
                }
                else{
                    passwordvalidator.setText("");
                    passwordvalidation = true;
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOTP = pinview.getText().toString();
                String password = passwordEditText.getText().toString();

                if(pinViewvalidation && passwordvalidation ){
                    saveuser(strOTP,password,usertoken);
                }
            }
        });

        resend(phoneNumber);
    }

    private void resend(final String phoneNumber) {
        countDownTimer1 = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                coundownandresendotpTextView.setText((millisUntilFinished/1000)+" sec");
            }

            @Override
            public void onFinish() {
                coundownandresendotpTextView.setText("Resend OTP");
                coundownandresendotpTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        resendOTP(phoneNumber);
                        coundownandresendotpTextView.setText("An OTP has been sent!");

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
//            bodyoptions.put("type", "getotp");
            bodyoptions.put("mobile",phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String options = bodyoptions.toString();
        RequestBody body = RequestBody.create(mediaType, options);
        Request request = new Request.Builder()
                .url(config.signup)
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
                            usertoken = jsonresponse.getJSONObject("data").getString("token");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    otpvalidator.setText("For Testing Purposes OTP: "+otp);

                                }
                            });

                        }
//                        else if(jsonresponse.getInt("errorCode") == 4){
//
//                            runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//
//                                    // Stuff that updates the UI
//                                    phoneValidatorTextView.setText("Phone not registered with company. Please register");
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

    private void saveuser(String strOTP, String password, String usertoken) {

        loadingDialogue.startLoadingDialogue();


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JSONObject bodyoptions = new JSONObject();

        try {
            bodyoptions.put("token",usertoken );
            bodyoptions.put("otp", strOTP);
            bodyoptions.put("password",password);
//            bodyoptions.put("mobile",phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String options = bodyoptions.toString();
        RequestBody body = RequestBody.create(mediaType, options);
        Request request = new Request.Builder()
                .url(config.saveuser)
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

                            Intent signupsuccessful = new Intent(SaveUserActivity.this, loginActivity.class);

                            startActivity(signupsuccessful);
                            finish();
                        }
                        else if(jsonresponse.getInt("errorCode") == 3){
                            loadingDialogue.dismissDialog();
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    otpvalidator.setText("wrong otp");
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
