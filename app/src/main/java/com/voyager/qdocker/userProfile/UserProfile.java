package com.voyager.qdocker.userProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.custom.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rimon on 21-03-2018.
 */

public class UserProfile extends AppCompatActivity {

    @BindView(R.id.userProfileToolBar)
    Toolbar userProfileToolBar;
    @BindView(R.id.userProfilePic)
    CircleImageView userProfilePic;
    @BindView(R.id.userProfileName)
    TextView userProfileName;
    @BindView(R.id.userProfileEmail)
    TextView userProfileEmail;

    UserDetails userDetails;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        bundle = new Bundle();
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if(userDetails!=null){
            System.out.println("AdminLanding -- userDetails- name : " + userDetails.getUserName());
            userProfileName.setText(userDetails.getUserName());
            userProfileEmail.setText(userDetails.getEmail());
            try{
                Picasso.with(this)
                        .load(userDetails.getUserPhotoUrl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(0, 200)
                        .into(userProfilePic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(getParent())
                                        .load(userDetails.getUserPhotoUrl())
                                        .error(R.drawable.profile)
                                        .resize(0, 200)
                                        .into(userProfilePic, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso","Could not fetch image");
                                            }
                                        });
                            }
                        });
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            System.out.println("AdminLanding -- userDetails- No data------ ");
        }

        setSupportActionBar(userProfileToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        userProfileToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
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