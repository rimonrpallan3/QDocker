package com.voyager.qdocker.AdminUserPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.voyager.qdocker.Landing.LandingPage;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.SignInPage;
import com.voyager.qdocker.SplashScreen.presenter.SplashPresenter;
import com.voyager.qdocker.SplashScreen.view.ISplashView;
import com.voyager.qdocker.custom.Helper;

/**
 * Created by User on 20-Mar-18.
 */

public class LoginChoicePage extends AppCompatActivity implements View.OnClickListener {

    LinearLayout admin;
    LinearLayout user;
    String adminExtra = "admin";
    String userExtra = "user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_choice_page);
        admin = (LinearLayout) findViewById(R.id.admin);
        user = (LinearLayout) findViewById(R.id.user);
        admin.setOnClickListener(this);
        user.setOnClickListener(this);

    }


    public void adminSignIn(){
        Intent intent = new Intent(this, SignInPage.class);
        intent.putExtra("admin", adminExtra);
        startActivity(intent);
        finish();
    }

    public void userSignIn(){
        Intent intent = new Intent(this, SignInPage.class);
        intent.putExtra("user", userExtra);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.admin) {
            adminSignIn();
        }
        if (i == R.id.user) {
            userSignIn();
        }
    }
}