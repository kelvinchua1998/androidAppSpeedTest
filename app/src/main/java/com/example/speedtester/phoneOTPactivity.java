package com.example.speedtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class phoneOTPactivity extends AppCompatActivity {

    EditText otpPhoneNumberEditText;
    Button otpNextButton;
    TextView phoneValidatorTextView;
    GlobalApplication.Config config = GlobalApplication.getconfiq();

    final LoadingDialogue loadingDialogue = new LoadingDialogue(phoneOTPactivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_o_t_pactivity);
        phoneValidatorTextView = (TextView) findViewById(R.id.phoneOTPValidatorTextView);
        otpPhoneNumberEditText = (EditText) findViewById(R.id.otpPhoneNumberEditText);
        otpNextButton = (Button) findViewById(R.id.otpNextButton);
        otpPhoneNumberEditText.selectAll();

        otpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNumber = otpPhoneNumberEditText.getText().toString();
                if(validation(phoneNumber)) {
                    getOTP(phoneNumber);
                }

            }
        });
    }

    private boolean validation(String phoneNumber){

        if(phoneNumber.length() != 8){

            phoneValidatorTextView.setText("please input a valid phone number!");
            return false;
        }else{
            phoneValidatorTextView.clearComposingText();
            return true;
        }


    }

    private void getOTP(final String phoneNumber){


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

                            Intent getotpSuccessful = new Intent(phoneOTPactivity.this, verifyActivity.class);
                            String phoneNumber1 = phoneNumber.toString();
                            getotpSuccessful.putExtra("phoneNumber", phoneNumber1);
//                            getotpSuccessful.putExtra("otp", jsonresponse.getJSONObject("data").getString("verifyCode"));
//                            getotpSuccessful.putExtra("usertoken", jsonresponse.getJSONObject("data").getInt("userCode"));

                            startActivity(getotpSuccessful);

                            finish();
                        }
                        else if(jsonresponse.getInt("errorCode") == 9){
                            loadingDialogue.dismissDialog();
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    phoneValidatorTextView.setText("Phone not registered. Please register");

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
