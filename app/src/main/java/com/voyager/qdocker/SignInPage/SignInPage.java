package com.voyager.qdocker.SignInPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.voyager.qdocker.adminAddDetails.AdminAddAddDetails;
import com.voyager.qdocker.UserLanding.UserLanding;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.SignInPage.presenter.ISignInPresenter;
import com.voyager.qdocker.SignInPage.presenter.SignInPresenter;
import com.voyager.qdocker.SignInPage.view.ISignInView;

/**
 * Created by rimon on 17-03-2018.
 */

public class SignInPage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ISignInView{

    private FirebaseAuth mAuth;
    String fireBaseToken;
    LinearLayout btnSignInGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInPage";
    private static final int RC_SIGN_IN = 9001;
    ISignInPresenter iSignInPresenter;
    FrameLayout loadingLayout;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    UserDetails userDetails;
    AdminDetails adminDetails;
    String adminExtra = "";
    String userExtra = "";
    String currentUser = "";

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_page);

        sharedPrefs = getSharedPreferences(getResources().getString(R.string.sharedPrefFileUserName),
                Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        Intent intent = getIntent();
        adminExtra = intent.getStringExtra("admin");
        userExtra = intent.getStringExtra("user");
        System.out.println("SignInPage adminExtra : "+adminExtra);
        System.out.println("SignInPage userExtra : "+userExtra);
        if(adminExtra!=null&& adminExtra.length()>0){
            currentUser = adminExtra;
        }else if(userExtra!=null&& userExtra.length()>0){
            currentUser = userExtra;
        }

        System.out.println("SignInPage currentUser : "+currentUser);
        btnSignInGoogle = (LinearLayout) findViewById(R.id.btnSignInGoogle);
        loadingLayout = (FrameLayout) findViewById(R.id.loadingLayout);

        mAuth = FirebaseAuth.getInstance();
        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("----------- onCreate ----------fireBaseToken: "+fireBaseToken);
            btnSignInGoogle.setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        iSignInPresenter = new SignInPresenter(this,mAuth,this,mGoogleSignInClient,currentUser);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUserFireBase = mAuth.getCurrentUser();
        iSignInPresenter.updateUI(currentUserFireBase,currentUser);
        /*if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                iSignInPresenter.firebaseAuthWithGoogle(account,currentUser);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    public void setLoader(int visibility){
        loadingLayout.setVisibility(visibility);
    }

    @Override
    public void storeValuePref(UserDetails userDetails) {
        this.userDetails =userDetails;
        Gson gson = new Gson();
        String jsonString = gson.toJson(userDetails);
        //UserDetails user1 = gson.fromJson(jsonString,UserDetails.class);
        if(jsonString!=null) {
            editor.putString(getResources().getString(R.string.sharedPrefFileUserName), jsonString);
            editor.commit();
            System.out.println("-----------validateLoginDataBaseApi UserDetails"+jsonString);
        }
    }

    @Override
    public void storeValueAdminPref(AdminDetails adminDetails) {
        this.adminDetails =adminDetails;
        Gson gson = new Gson();
        String jsonString = gson.toJson(adminDetails);
        //UserDetails user1 = gson.fromJson(jsonString,UserDetails.class);
        if(jsonString!=null) {
            editor.putString(getResources().getString(R.string.sharedPrefFileAdmin), jsonString);
            editor.commit();
            System.out.println("-----------validateLoginDataBaseApi UserDetails"+jsonString);
        }
    }

    @Override
    public void gotLanding() {
        Intent intent = new Intent(this, UserLanding.class);
        intent.putExtra("UserDetails", userDetails);
        startActivity(intent);
        finish();
    }

    @Override
    public void goSuddenLanding(Object object, String currentUser) {
        if(currentUser.equals("admin")){
            adminDetails = (AdminDetails) object;
            Intent intent = new Intent(this, AdminAddAddDetails.class);
            intent.putExtra("AdminDetails", adminDetails);
            startActivity(intent);
            finish();
        }else if(currentUser.equals("user")){
            userDetails = (UserDetails) object;
            Intent intent = new Intent(this, UserLanding.class);
            intent.putExtra("UserDetails", userDetails);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void goGetAdminDetails(AdminDetails adminDetails) {
        Intent intent = new Intent(this, AdminAddAddDetails.class);
        intent.putExtra("AdminDetails", adminDetails);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnSignInGoogle) {
            signIn();
        }
    }

    /**
     * This method starts the google sign in process.
     * It opens the dialog box for choosing google account.
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END auth_with_google]

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}