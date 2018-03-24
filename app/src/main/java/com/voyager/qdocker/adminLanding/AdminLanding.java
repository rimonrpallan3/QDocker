package com.voyager.qdocker.adminLanding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.adminProfile.AdminProfile;
import com.voyager.qdocker.adminAbout.AdminAbout;
import com.voyager.qdocker.custom.CircleImageView;
import com.voyager.qdocker.custom.Helper;
import com.voyager.qdocker.loginchoice.LoginChoicePage;

import butterknife.ButterKnife;

/**
 * Created by rimon on 21-03-2018.
 */

public class AdminLanding extends AppCompatActivity {

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String imei = "";
    String meid = "";
    private FirebaseAuth mAuth;
    String fireBaseToken;
    Bundle bundle;
    AdminDetails adminDetails;

    Activity activity;
    public Toolbar adminToolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationViewAdmin;
    private DrawerLayout mDrawerLayoutAdmin;
    CircleImageView customerProfileDrawerImg;
    TextView customerProfileDrawerTitle;
    private DatabaseReference mDatabase;

    FrameLayout landingAdminContainer;
    ImageView choseTripBackPressAdmin;
    private final static double DRAWER_COVER_ASPECT_RATIO = 9d / 14d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_admin_page);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        sharedPrefs = getSharedPreferences(getResources().getString(R.string.sharedPrefFileAdmin),
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        Intent intent = getIntent();
        bundle = new Bundle();
        adminDetails = (AdminDetails) intent.getParcelableExtra("AdminDetails");
        if(adminDetails!=null){
            System.out.println("AdminLanding -- AdminDetails- name : " + adminDetails.getUserName());
        } else {
            getAdminDetails();
        }

        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        adminToolbar = (Toolbar) findViewById(R.id.adminToolbar);
        setSupportActionBar(adminToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        adminToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        landingAdminContainer = (FrameLayout) findViewById(R.id.landingAdminContainer);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        //  Navigation Drawer
        navigationViewAdmin = (NavigationView) findViewById(R.id.navigationViewAdmin);
        mDrawerLayoutAdmin = (DrawerLayout) findViewById(R.id.drawerLayoutAdmin);
        choseTripBackPressAdmin = (ImageButton) findViewById(R.id.choseTripBackPressAdmin);

        addDrawerItems();
        setupDrawer();
        //------------ End of Navigation Drawer-----------------------------
        AdminLandingFragment adminLandingFragment = new AdminLandingFragment(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.landingAdminContainer, adminLandingFragment);
        adminLandingFragment.setArguments(bundle);
        fragmentTransaction.commit();
        int width = (getResources().getDisplayMetrics().widthPixels);
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationViewAdmin.getLayoutParams();
        params.width = (int) (width * DRAWER_COVER_ASPECT_RATIO);
        navigationViewAdmin.setLayoutParams(params);
    }

    private void getAdminDetails() {
        Gson gson = new Gson();
        String json = sharedPrefs.getString(getResources().getString(R.string.sharedPrefFileAdmin),null);
        if(json!=null){
            System.out.println("-----------AdminLanding getAdminDetails adminDetails" + json);
            adminDetails = gson.fromJson(json, AdminDetails.class);
            //emailAddress = userDetails.getEmail();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:

                if (mDrawerToggle.isDrawerIndicatorEnabled()) {
                    mDrawerLayoutAdmin.openDrawer(GravityCompat.START);

                    customerProfileDrawerImg = (CircleImageView) navigationViewAdmin.findViewById(R.id.customerProfileDrawerImg);
                    customerProfileDrawerTitle = (TextView) navigationViewAdmin.findViewById(R.id.customerProfileDrawerTitle);
                    customerProfileDrawerTitle.setText(adminDetails.getUserName());
                    try{
                        Picasso.with(this)
                                .load(adminDetails.getUserPhotoUrl())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .resize(0, 200)
                                .into(customerProfileDrawerImg, new Callback() {
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
                                                .into(customerProfileDrawerImg, new Callback() {
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
                    onBackPressed();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * this method is used to set the Boolean Check in what state its current in
     *
     * @param isEnabled Boolean value to state open or close
     */
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayoutAdmin.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
            this.getSupportActionBar().setHomeButtonEnabled(true);

        } else {
            mDrawerLayoutAdmin.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.syncState();
            this.getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    /**
     * this method is used to set up navigation drawer Close and open states
     */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayoutAdmin, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //mDrawerLayoutAdmin.openDrawer(Gravity.RIGHT);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };

        mDrawerLayoutAdmin.setDrawerListener(mDrawerToggle);
        mDrawerLayoutAdmin.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * this method is used to set the drawer item and initializes its click event in navigation drawer.
     */
    private void addDrawerItems() {
        navigationViewAdmin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(false);
                //Closing drawer on item click
                mDrawerLayoutAdmin.closeDrawers();
                //Check to see which item was being clicked and perform appropriate action
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.adminUpdateProfile:
                        Intent intent = new Intent(AdminLanding.this, AdminProfile.class);
                        intent.putExtra("AdminDetails", adminDetails);
                        startActivity(intent);
                        //   getSupportActionBar().setTitle(getString(R.string.profile));
                        //  faith_main_activity_following_tab_layout.setVisibility(View.INVISIBLE);
                        return true;

                    // For rest of the options we just show a toast on click


                    case R.id.adminAbout:
                        intent = new Intent(AdminLanding.this, AdminAbout.class);
                        intent.putExtra("AdminDetails", adminDetails);
                        startActivity(intent);

                        return true;

                    case R.id.adminLogout:
                        mDatabase.child("admin").child(adminDetails.getUserId()).child("status").setValue(false);
                        editor.remove(getResources().getString(R.string.sharedPrefFileAdmin));
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        intent = new Intent(AdminLanding.this, LoginChoicePage.class);
                        startActivity(intent);
                        finish();

                        return true;

                    default:

                        return true;

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            navigationViewAdmin.getMenu().getItem(0);
            setDrawerState(true);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.landingAdminContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Helper.REQUEST_LOCATION_CHECK_SETTINGS) {
        }
    }
}