package com.voyager.qdocker.adminProfile;

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
import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.custom.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rimon on 21-03-2018.
 */

public class AdminProfile extends AppCompatActivity {

    @BindView(R.id.adminProfileToolBar)
    Toolbar adminProfileToolBar;
    @BindView(R.id.adminProfilePic)
    CircleImageView adminProfilePic;
    @BindView(R.id.adminProfileName)
    TextView adminProfileName;
    @BindView(R.id.adminProfileEmail)
    TextView adminProfileEmail;
    @BindView(R.id.adminRegNo)
    TextView adminRegNo;
    @BindView(R.id.adminOrgType)
    TextView adminOrgType;

    AdminDetails adminDetails;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_admin);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        bundle = new Bundle();
        adminDetails = (AdminDetails) intent.getParcelableExtra("AdminDetails");
        if(adminDetails!=null){
            System.out.println("AdminLanding -- AdminDetails- name : " + adminDetails.getUserName());
            adminProfileName.setText(adminDetails.getUserName());
            adminProfileEmail.setText(adminDetails.getEmail());
            adminRegNo.setText(adminDetails.getAdminRegNo());
            adminOrgType.setText(adminDetails.getAdminQrgType());
            try{
                Picasso.with(this)
                        .load(adminDetails.getUserPhotoUrl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(0, 200)
                        .into(adminProfilePic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(getParent())
                                        .load(adminDetails.getUserPhotoUrl())
                                        .error(R.drawable.profile)
                                        .resize(0, 200)
                                        .into(adminProfilePic, new Callback() {
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
            System.out.println("AdminLanding -- AdminDetails- No data------ ");
        }

        setSupportActionBar(adminProfileToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        adminProfileToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));

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