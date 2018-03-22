package com.voyager.qdocker.loginchoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.SignInPage;

/**
 * Created by User on 20-Mar-18.
 */

public class LoginChoicePage extends AppCompatActivity implements View.OnClickListener {

    LinearLayout admin;
    LinearLayout user;
    String adminExtra = "";
    String userExtra = "";


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
        userExtra = "user";
        Intent intent = new Intent(this, SignInPage.class);
        intent.putExtra("admin", adminExtra);
        startActivity(intent);
        finish();
    }

    public void userSignIn(){
        adminExtra = "admin";
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