package com.voyager.qdocker.Landing;

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
import com.voyager.qdocker.SplashScreen.presenter.IConnectionStatus;
import com.voyager.qdocker.SplashScreen.presenter.SplashPresenter;
import com.voyager.qdocker.SplashScreen.view.ISplashView;
import com.voyager.qdocker.custom.Helper;

/**
 * Created by rimon on 17-03-2018.
 */

public class LandingPage extends AppCompatActivity implements ISplashView {

    private IConnectionStatus mPresenter;
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
        setContentView(R.layout.landing_page);
        mAuth = FirebaseAuth.getInstance();

        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        mPresenter = new SplashPresenter(this,this,this,sharedPrefs,editor);
        Intent intent = getIntent();
        bundle = new Bundle();
        String hiddenBtn = intent.getStringExtra("btnHiddenBtn");
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        adminDetails = (AdminDetails) intent.getParcelableExtra("AdminDetails");
        if (userDetails != null) {
            System.out.println("LandingPage -- UserDetails- name : " + userDetails.getUserName());
        }else if(adminDetails!=null){
            System.out.println("LandingPage -- UserDetails- name : " + adminDetails.getUserName());
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
        String json = sharedPrefs.getString(getResources().getString(R.string.sharedPrefFileName),null);
        if(json!=null){
            System.out.println("-----------LandingPage uploadProfileName UserDetails" + json);
            userDetails = gson.fromJson(json, UserDetails.class);
            //emailAddress = userDetails.getEmail();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.REQUEST_LOCATION_CHECK_SETTINGS) {
            mPresenter.load();
        }
    }

    @Override
    public void moveToSignUpLogin() {

    }

    @Override
    public void moveToLanding() {

    }
}