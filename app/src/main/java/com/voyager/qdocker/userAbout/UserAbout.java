package com.voyager.qdocker.userAbout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.voyager.qdocker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rimon on 21-03-2018.
 */

public class UserAbout extends AppCompatActivity {

    @BindView(R.id.userToolbarAboutPage)
    Toolbar userToolbarAboutPage;
    @BindView(R.id.tvUserAboutContent)
    TextView tvUserAboutContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_user);
        ButterKnife.bind(this);

        setSupportActionBar(userToolbarAboutPage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.about));
        userToolbarAboutPage.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
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