package com.voyager.qdocker.SplashScreen.presenter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.SplashScreen.view.ISplashView;

/**
 * Created by User on 8/28/2017.
 */

public class SplashPresenter implements IConnectionStatus{

    Context context;
    ISplashView iSplashView;
    Activity activity;
    String emailAddress;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    private int SPLASH_DISPLAY_LENGTH = 1000;

    public SplashPresenter(Context context, ISplashView iSplashView, Activity activity, SharedPreferences sharedPrefs, SharedPreferences.Editor editor) {
        this.activity = activity;
        this.context = context;
        this.iSplashView = iSplashView;
        this.sharedPrefs = sharedPrefs;
        this.editor = editor;
        emailAddress = getUserGsonInSharedPrefrences();
    }

    public String getUserGsonInSharedPrefrences(){
        String emailAddress ="";
        Gson gson = new Gson();
        String json = sharedPrefs.getString(context.getResources().getString(R.string.sharedPrefFileName), null);
        if(json!=null){
            UserDetails userDetails = gson.fromJson(json, UserDetails.class);
            emailAddress = userDetails.getEmail();
            System.out.println("--------- SplashPresenter getUserGsonInSharedPrefrences"+json);
        }
        return emailAddress;
    }

    @Override
    public void load() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(emailAddress.length()>0){
                        iSplashView.moveToLanding();
                    }else{
                        iSplashView.moveToSignUpLogin();
                    }

            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
