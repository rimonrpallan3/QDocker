package com.voyager.qdocker.UserLanding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.custom.Helper;

/**
 * Created by rimon on 17-03-2018.
 */

public class UserLanding extends AppCompatActivity  {

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String imei = "";
    String meid = "";
    private FirebaseAuth mAuth;
    String fireBaseToken;
    Bundle bundle;
    UserDetails userDetails;
    AdminDetails adminDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_user_page);
        mAuth = FirebaseAuth.getInstance();

        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        Intent intent = getIntent();
        bundle = new Bundle();
        String hiddenBtn = intent.getStringExtra("btnHiddenBtn");
        adminDetails = (AdminDetails) intent.getParcelableExtra("AdminDetails");
        if (userDetails != null) {
            System.out.println("UserLanding -- UserDetails- name : " + userDetails.getUserName());
        } else {
            getUserSDetails();
        }

        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("----------- onCreate ----------fireBaseToken: "+fireBaseToken);
        try{

       }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getUserSDetails() {
        Gson gson = new Gson();
        String json = sharedPrefs.getString(getResources().getString(R.string.sharedPrefFileUserName),null);
        if(json!=null){
            System.out.println("-----------UserLanding uploadProfileName UserDetails" + json);
            userDetails = gson.fromJson(json, UserDetails.class);
            //emailAddress = userDetails.getEmail();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.REQUEST_LOCATION_CHECK_SETTINGS) {
        }
    }
}