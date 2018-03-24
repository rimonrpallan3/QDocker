package com.voyager.qdocker.SplashScreen;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.adminLanding.AdminLanding;
import com.voyager.qdocker.common.Config;
import com.voyager.qdocker.loginchoice.LoginChoicePage;
import com.voyager.qdocker.UserLanding.UserLanding;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SplashScreen.presenter.SplashPresenter;
import com.voyager.qdocker.SplashScreen.view.ISplashView;
import com.voyager.qdocker.custom.Helper;


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
        Config.FIREBASE_URL = getResources().getString(R.string.fireBaseUrl);
        Config.FIREBASE_STORAGE_URL = getResources().getString(R.string.fireBaseStorageUrl);
        mAuth = FirebaseAuth.getInstance();

        sharedPrefs = getSharedPreferences(getResources().getString(R.string.sharedPrefFileUserName),
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        mPresenter = new SplashPresenter(this,this,this,sharedPrefs);
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
        Intent intent = new Intent(this, LoginChoicePage.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void moveToUserLanding(UserDetails userDetails) {
        Intent intent = new Intent(this, UserLanding.class);
        intent.putExtra("UserDetails", userDetails);
        startActivity(intent);
        finish();
    }

    @Override
    public void moveToAdminLanding(AdminDetails adminDetails) {
        Intent intent = new Intent(this, AdminLanding.class);
        intent.putExtra("AdminDetails", adminDetails);
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