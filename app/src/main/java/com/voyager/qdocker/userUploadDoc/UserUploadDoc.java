package com.voyager.qdocker.userUploadDoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.voyager.qdocker.R;

import butterknife.ButterKnife;

/**
 * Created by User on 22-Mar-18.
 */

public class UserUploadDoc extends AppCompatActivity {

    Activity activity;
    public Toolbar userUploadDocToolbar;
    private FirebaseAuth mAuth;
    String fireBaseToken;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_upload_doc);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        userUploadDocToolbar = (Toolbar) findViewById(R.id.userUploadDocToolbar);
        setSupportActionBar(userUploadDocToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        userUploadDocToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
