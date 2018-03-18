package com.voyager.qdocker.SplashScreen;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.voyager.qdocker.Landing.LandingPage;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SplashScreen.presenter.SplashPresenter;
import com.voyager.qdocker.SplashScreen.view.ISplashView;
import com.voyager.qdocker.custom.Helper;

import static com.voyager.qdocker.custom.Helper.REQUEST_PHONE_STATE;


/**
 * Created by User on 8/23/2017.
 */

public class SplashScreen extends AppCompatActivity implements ISplashView {

    private SplashPresenter mPresenter;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String imei = "";
    String meid = "";
    private FirebaseAuth mAuth;
    String fireBaseToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mAuth = FirebaseAuth.getInstance();

        sharedPrefs = getSharedPreferences(Helper.UserDetails,
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        mPresenter = new SplashPresenter(this,this,this,sharedPrefs,editor);
        mPresenter.load();
        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("----------- onCreate ----------fireBaseToken: "+fireBaseToken);
        // getDeviceIMEI();
        try{
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("----------- onCreate ----------imei: "+imei+", meid: "+meid);
    }


    @Override
    public void moveToSignUpLogin() {
        /*Intent intent = new Intent(this, LoginSignUpPage.class);
        startActivity(intent);
        finish();*/
    }

    @Override
    public void moveToLanding() {
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.REQUEST_LOCATION_CHECK_SETTINGS) {
            mPresenter.load();
        }
    }
}