package com.voyager.qdocker.SplashScreen.presenter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.SplashScreen.view.ISplashView;

/**
 * Created by User on 8/28/2017.
 */

public class SplashPresenter implements ISplashPresenter {

    Context context;
    ISplashView iSplashView;
    Activity activity;
    String userEmailAddress;
    String adminEmailAddress;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    AdminDetails adminDetails;
    UserDetails userDetails;

    private int SPLASH_DISPLAY_LENGTH = 1000;

    public SplashPresenter(Context context, ISplashView iSplashView, Activity activity, SharedPreferences sharedPrefs) {
        this.activity = activity;
        this.context = context;
        this.iSplashView = iSplashView;
        this.sharedPrefs = sharedPrefs;
        userEmailAddress = getUserGsonInSharedPrefrences();
        adminEmailAddress = getAdminGsonInSharedPrefrences();
    }

    public String getUserGsonInSharedPrefrences(){
        String emailAddress ="";
        Gson gson = new Gson();
        String json = sharedPrefs.getString(context.getResources().getString(R.string.sharedPrefFileUserName), null);
        if(json!=null){
            userDetails = gson.fromJson(json, UserDetails.class);
            emailAddress = userDetails.getEmail();
            System.out.println("--------- SplashPresenter getUserGsonInSharedPrefrences"+json);
        }
        return emailAddress;
    }
    public String getAdminGsonInSharedPrefrences(){
        String emailAddress ="";
        Gson gson = new Gson();
        String json = sharedPrefs.getString(context.getResources().getString(R.string.sharedPrefFileAdmin), null);
        if(json!=null){
            adminDetails = gson.fromJson(json, AdminDetails.class);
            emailAddress = adminDetails.getEmail();
            System.out.println("--------- SplashPresenter getAdminGsonInSharedPrefrences"+json);
        }
        return emailAddress;
    }

    @Override
    public void load() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(userEmailAddress.length()>0){
                        iSplashView.moveToUserLanding(userDetails);
                    }else if(adminEmailAddress.length()>0){
                        iSplashView.moveToAdminLanding(adminDetails);
                    }else{
                        iSplashView.moveToSignUpLogin();
                    }

            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
