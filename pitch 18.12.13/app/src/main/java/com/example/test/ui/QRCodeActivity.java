package com.example.test.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.twofa.Statics;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class QRCodeActivity extends Activity {

    private String qrCodeCallUrl,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        /** get auth creds from previous activity **/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId= extras.getString("userId");
        }
        qrCodeCallUrl="https://api.authy.com/protected/json/users/"+userId+"/secret?api_key=AIzaSyAdDofn5TFuByjtqdTpLna5zRk4gx9s05M";

        /** call authy api to get qr code **/
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,qrCodeCallUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String qrCodePath = response.getString("qr_code");
                            /** set the imageView's src **/
                            ImageView qrCodeImgVw = findViewById(R.id.qrCodeImgVw);
                            Glide.with(qrCodeImgVw).load(qrCodePath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR! ",error.getMessage());
                    }
                });
//        (AppSingleton.getInstance(getApplicationContext()).getRequestQueue()).add(jsObjRequest);

        /** pass the code provided by user to the Authy API to verify it **/
        (findViewById(R.id.confirmSignupBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Statics.validateSecurityCode(((EditText)findViewById(R.id.validationCode)).getText().toString(),userId,QRCodeActivity.this,
                        ((EditText)findViewById(R.id.validationCode)),((TextView)findViewById(R.id.errorTxt)));
            }
        });
    }

    /** for calligraphy lib usage **/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
