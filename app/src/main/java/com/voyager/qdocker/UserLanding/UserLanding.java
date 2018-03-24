package com.voyager.qdocker.UserLanding;

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
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.adminLanding.AdminLandingFragment;
import com.voyager.qdocker.adminProfile.AdminProfile;
import com.voyager.qdocker.adminAbout.AdminAbout;
import com.voyager.qdocker.custom.CircleImageView;
import com.voyager.qdocker.custom.Helper;
import com.voyager.qdocker.loginchoice.LoginChoicePage;
import com.voyager.qdocker.userAbout.UserAbout;
import com.voyager.qdocker.userProfile.UserProfile;

/**
 * Created by rimon on 17-03-2018.
 */

public class UserLanding extends AppCompatActivity  {

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String imei = "";
    String meid = "";
    private FirebaseAuth mAuth;
    String fireBaseToken;
    Bundle bundle;
    UserDetails userDetails;
    

    Activity activity;
    public Toolbar userToolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationViewUser;
    private DrawerLayout drawerLayoutUser;
    CircleImageView customerProfileDrawerImg;
    TextView customerProfileDrawerTitle;
    private DatabaseReference mDatabase;

    FrameLayout landingUserContainer;
    ImageView choseTripBackPressUser;
    private final static double DRAWER_COVER_ASPECT_RATIO = 9d / 14d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_user_page);
        mAuth = FirebaseAuth.getInstance();

        sharedPrefs = getSharedPreferences(getResources().getString(R.string.sharedPrefFileUserName),
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        Intent intent = getIntent();
        bundle = new Bundle();
        String hiddenBtn = intent.getStringExtra("btnHiddenBtn");
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if (userDetails != null) {
            System.out.println("UserLanding -- UserDetails- name : " + userDetails.getUserName());
        } else {
            getUserSDetails();
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        userToolbar = (Toolbar) findViewById(R.id.userToolbar);
        setSupportActionBar(userToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        userToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        landingUserContainer = (FrameLayout) findViewById(R.id.landingUserContainer);


        //  Navigation Drawer
        navigationViewUser = (NavigationView) findViewById(R.id.navigationViewUser);
        drawerLayoutUser = (DrawerLayout) findViewById(R.id.drawerLayoutUser);
        choseTripBackPressUser = (ImageButton) findViewById(R.id.choseTripBackPressUser);

        addDrawerItems();
        setupDrawer();
        //------------ End of Navigation Drawer-----------------------------
        UserLandingFragment userLandingFragment = new UserLandingFragment(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.landingUserContainer, userLandingFragment);
        userLandingFragment.setArguments(bundle);
        fragmentTransaction.commit();
        int width = (getResources().getDisplayMetrics().widthPixels);
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationViewUser.getLayoutParams();
        params.width = (int) (width * DRAWER_COVER_ASPECT_RATIO);
        navigationViewUser.setLayoutParams(params);
    }

    private void getUserSDetails() {
        Gson gson = new Gson();
        String json = sharedPrefs.getString(getResources().getString(R.string.sharedPrefFileUserName),null);
        if(json!=null){
            System.out.println("-----------UserLanding uploadProfileName UserDetails" + json);
            userDetails = gson.fromJson(json, UserDetails.class);
            //emailAddress = userDetails.getEmail();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:

                if (mDrawerToggle.isDrawerIndicatorEnabled()) {
                    drawerLayoutUser.openDrawer(GravityCompat.START);

                    customerProfileDrawerImg = (CircleImageView) navigationViewUser.findViewById(R.id.customerProfileDrawerImg);
                    customerProfileDrawerTitle = (TextView) navigationViewUser.findViewById(R.id.customerProfileDrawerTitle);
                    customerProfileDrawerTitle.setText(userDetails.getUserName());
                    try{
                        Picasso.with(this)
                                .load(userDetails.getUserPhotoUrl())
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
                                                .load(userDetails.getUserPhotoUrl())
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
            drawerLayoutUser.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
            this.getSupportActionBar().setHomeButtonEnabled(true);

        } else {
            drawerLayoutUser.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutUser, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //drawerLayoutUser.openDrawer(Gravity.RIGHT);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };

        drawerLayoutUser.setDrawerListener(mDrawerToggle);
        drawerLayoutUser.post(new Runnable() {
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
        navigationViewUser.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(false);
                //Closing drawer on item click
                drawerLayoutUser.closeDrawers();
                //Check to see which item was being clicked and perform appropriate action
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.userUpdateProfile:
                        Intent intent = new Intent(UserLanding.this, UserProfile.class);
                        intent.putExtra("UserDetails", userDetails);
                        startActivity(intent);
                        //   getSupportActionBar().setTitle(getString(R.string.profile));
                        //  faith_main_activity_following_tab_layout.setVisibility(View.INVISIBLE);
                        return true;

                    // For rest of the options we just show a toast on click


                    case R.id.userAbout:
                        intent = new Intent(UserLanding.this, UserAbout.class);
                        intent.putExtra("UserDetails", userDetails);
                        startActivity(intent);

                        return true;

                    case R.id.userLogout:
                        mDatabase.child("user").child(userDetails.getUserId()).child("status").setValue(false);
                        editor.remove(getResources().getString(R.string.sharedPrefFileUserName));
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        intent = new Intent(UserLanding.this, LoginChoicePage.class);
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
            navigationViewUser.getMenu().getItem(0);
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