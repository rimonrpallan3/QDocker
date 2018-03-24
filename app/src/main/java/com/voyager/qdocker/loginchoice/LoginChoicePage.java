package com.voyager.qdocker.loginchoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.SignInPage;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by User on 20-Mar-18.
 */

public class LoginChoicePage extends AppCompatActivity {

    LinearLayout admin;
    LinearLayout user;
    String adminExtra = "";
    String userExtra = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_choice_page);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.admin)
    public void adminSignIn(){
        adminExtra = "admin";
        Intent intent = new Intent(this, SignInPage.class);
        intent.putExtra("admin", adminExtra);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.user)
    public void userSignIn(){
        userExtra = "user";
        Intent intent = new Intent(this, SignInPage.class);
        intent.putExtra("user", userExtra);
        startActivity(intent);
        finish();
    }
}