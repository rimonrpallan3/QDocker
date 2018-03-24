package com.voyager.qdocker.SignInPage.presenter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.SignInPage.model.IUserDetials;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.SignInPage.view.ISignInView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rimon on 18-03-2018.
 */

public class SignInPresenter implements ISignInPresenter{

    ISignInView iSignInView;
    private static final String TAG = "SignInPresenter";
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseUser user;
    Activity activity;
    IUserDetials iUserDetials;
    UserDetails userDetails;
    AdminDetails adminDetails;
    String urlPhoto;
    GoogleSignInClient mGoogleSignInClient;
    Boolean state = false;
    String userId = "";
    String userName = "";
    String userEmailAdress = "";
    String userImageUrl = "";
    String app_version = "";
    String userMob ="";
    Map<String, Object> current_app_version = new HashMap<>();
    DatabaseReference mDatabase;
    String currentUser="";

    public SignInPresenter(ISignInView iSignInView, FirebaseAuth mAuth, Activity activity, GoogleSignInClient mGoogleSignInClient, String currentUser) {
        this.iSignInView = iSignInView;
        this.mAuth = mAuth;
        this.activity = activity;
        this.mGoogleSignInClient = mGoogleSignInClient;
        this.currentUser = currentUser;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        startloginIn();
    }

    /**
     * Once the Google Sign in is successful,
     * Firebase authentication is done using Google's Sign In credentials.
     * And finally the users details are stored on the Firebase Authentication console.
     *
     * @param acct  Google's Sign in account.
     */
    // [START auth_with_google]
    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct,final String currentUser) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        iSignInView.setLoader(View.VISIBLE);
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("Login Anonymous success: currentUser: "+currentUser);
                            updateUI(user,currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(activity.findViewById(android.R.id.content),activity.getResources().getString(R.string.snackErrorMsg), Snackbar.LENGTH_SHORT).show();
                           /* Snackbar.make(getParent().findViewById(android.R.id.content),
                                    getResources().getString(R.string.snackErrorMsg),
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getResources().getString(R.string.snackOkBtn), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            btnSignInGoogle.setVisibility(View.GONE);
                                        }
                                    }).show();*/
                            updateUI(null, currentUser);
                        }

                        // [START_EXCLUDE]
                        iSignInView.setLoader(View.GONE);
                        // [END_EXCLUDE]
                    }
                });
    }
   /* @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        iSignInView.setLoader(View.VISIBLE);
        // [END_EXCLUDE]

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("Login : login with google "+task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Snackbar.make(activity.findViewById(android.R.id.content),
                                    activity.getResources().getString(R.string.snackErrorMsg),
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction(activity.getResources().getString(R.string.snackOkBtn), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //sign_in_button.setVisibility(View.GONE);
                                        }
                                    }).show();
                        }

                    }
                });
    }*/

    public void startloginIn(){

        if(currentUser.equals("admin")){
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    user = firebaseAuth.getCurrentUser();
                    if (user != null && !(user.isAnonymous())) {
                        state = true;
                        userId = user.getUid();
                        userName = user.getDisplayName();
                        userEmailAdress = user.getEmail();
                        userImageUrl = String.valueOf(user.getPhotoUrl());
                        userMob = user.getPhoneNumber();

                        if(userImageUrl.equals("null"))
                            userImageUrl ="";

                        FirebaseDatabase.getInstance()
                                .getReference("admin")
                                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                        ArrayList<String> allusersList = new ArrayList<>();
                                        for (com.google.firebase.database.DataSnapshot allusers : dataSnapshot.getChildren()){
                                            allusersList.add(allusers.getKey());
                                        }


                                        if (allusersList.contains(userId)){
                                            Map<String, Object> userInfo_map = new HashMap<>();
                                            userInfo_map.put("name", userName);
                                            userInfo_map.put("email", userEmailAdress);
                                            userInfo_map.put("user_image", userImageUrl);
                                            FirebaseDatabase.getInstance()
                                                    .getReference("admin")
                                                    .child(userId)
                                                    .updateChildren(userInfo_map);

                                            Map<String, Object> fcm_reg_token = new HashMap<>();
                                            fcm_reg_token.put("fcm_reg_token", FirebaseInstanceId.getInstance().getToken());
                                            FirebaseDatabase.getInstance().getReference("admin")
                                                    .child(user.getUid()).updateChildren(fcm_reg_token);

                                            try {
                                                app_version = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
                                            } catch (PackageManager.NameNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            current_app_version.put("current_app_version", app_version);
                                            FirebaseDatabase.getInstance().getReference("admin")
                                                    .child(user.getUid()).updateChildren(current_app_version);

                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("developer");
                                            adminDetails = new AdminDetails(state, userId, userEmailAdress, userName, userImageUrl, userMob);
                                            iSignInView.storeValueAdminPref(adminDetails);
                                            iSignInView.goSuddenLanding(adminDetails,currentUser);
                                        } else{
                                            FirebaseDatabase.getInstance()
                                                    .getReference("admin")
                                                    .child(userId)
                                                    .setValue(new UserDetails(state, userId, userEmailAdress, userName, userImageUrl, userMob))
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Map<String, Object> fcm_reg_token = new HashMap<>();
                                                            fcm_reg_token.put("fcm_reg_token", FirebaseInstanceId.getInstance().getToken());
                                                            FirebaseDatabase.getInstance().getReference("admin")
                                                                    .child(user.getUid()).updateChildren(fcm_reg_token);

                                                            try {
                                                                app_version = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
                                                            } catch (PackageManager.NameNotFoundException e) {
                                                                e.printStackTrace();
                                                            }
                                                            current_app_version.put("current_app_version", app_version);
                                                            FirebaseDatabase.getInstance().getReference("admin")
                                                                    .child(user.getUid()).updateChildren(current_app_version);

                                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("developer");

                                                            adminDetails = new AdminDetails(state, userId, userEmailAdress, userName, userImageUrl, userMob);
                                                            iSignInView.storeValueAdminPref(adminDetails);
                                                            iSignInView.goSuddenLanding(adminDetails,currentUser);
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            };
        }else if(currentUser.equals("user")){
             mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    user = firebaseAuth.getCurrentUser();
                    if (user != null && !(user.isAnonymous())) {
                        state = true;
                        userId = user.getUid();
                        userName = user.getDisplayName();
                        userEmailAdress = user.getEmail();
                        userImageUrl = String.valueOf(user.getPhotoUrl());
                        userMob = user.getPhoneNumber();

                        if(userImageUrl.equals("null"))
                            userImageUrl ="";

                        FirebaseDatabase.getInstance()
                                .getReference("users")
                                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                        ArrayList<String> allusersList = new ArrayList<>();
                                        for (com.google.firebase.database.DataSnapshot allusers : dataSnapshot.getChildren()){
                                            allusersList.add(allusers.getKey());
                                        }


                                        if (allusersList.contains(userId)){
                                            Map<String, Object> userInfo_map = new HashMap<>();
                                            userInfo_map.put("name", userName);
                                            userInfo_map.put("email", userEmailAdress);
                                            userInfo_map.put("user_image", userImageUrl);
                                            FirebaseDatabase.getInstance()
                                                    .getReference("users")
                                                    .child(userId)
                                                    .updateChildren(userInfo_map);

                                            Map<String, Object> fcm_reg_token = new HashMap<>();
                                            fcm_reg_token.put("fcm_reg_token", FirebaseInstanceId.getInstance().getToken());
                                            FirebaseDatabase.getInstance().getReference("users")
                                                    .child(user.getUid()).updateChildren(fcm_reg_token);

                                            try {
                                                app_version = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
                                            } catch (PackageManager.NameNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            current_app_version.put("current_app_version", app_version);
                                            FirebaseDatabase.getInstance().getReference("users")
                                                    .child(user.getUid()).updateChildren(current_app_version);

                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("developer");
                                            userDetails = new UserDetails(state, userId, userEmailAdress, userName, userImageUrl, userMob);
                                            iSignInView.storeValuePref(userDetails);
                                            iSignInView.goSuddenLanding(userDetails,currentUser);
                                        } else{
                                            FirebaseDatabase.getInstance()
                                                    .getReference("users")
                                                    .child(userId)
                                                    .setValue(new UserDetails(state, userId, userEmailAdress, userName, userImageUrl, userMob))
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Map<String, Object> fcm_reg_token = new HashMap<>();
                                                            fcm_reg_token.put("fcm_reg_token", FirebaseInstanceId.getInstance().getToken());
                                                            FirebaseDatabase.getInstance().getReference("users")
                                                                    .child(user.getUid()).updateChildren(fcm_reg_token);

                                                            try {
                                                                app_version = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
                                                            } catch (PackageManager.NameNotFoundException e) {
                                                                e.printStackTrace();
                                                            }
                                                            current_app_version.put("current_app_version", app_version);
                                                            FirebaseDatabase.getInstance().getReference("users")
                                                                    .child(user.getUid()).updateChildren(current_app_version);

                                                            FirebaseMessaging.getInstance().unsubscribeFromTopic("developer");

                                                            userDetails = new UserDetails(state, userId, userEmailAdress, userName, userImageUrl, userMob);
                                                            iSignInView.storeValuePref(userDetails);
                                                            iSignInView.goSuddenLanding(userDetails,currentUser);

                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            };
        }


    }


    public void firebaseAuthWithAnonymous(final GoogleSignInAccount acct ,final String currentUser) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        mAuth.getCurrentUser();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("Login Anonymous Linking: "+task.isSuccessful());
                        Log.d(TAG, "linkWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            System.out.println("Login initate login with google");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User account deleted.");
                                        System.out.println("onComplete User account deleted.");
                                    }
                                }
                            });
                            firebaseAuthWithGoogle(acct,currentUser);
                        }

                    }
                });
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END auth_with_google]
    @Override
    public void updateUI(FirebaseUser user,String  currentUser) {
        iSignInView.setLoader(View.GONE);
        if (user != null) {
            state = true;
            userId = user.getUid();
            userName = user.getDisplayName();
            userEmailAdress = user.getEmail();
            userImageUrl = String.valueOf(user.getPhotoUrl());
            userMob = user.getPhoneNumber();
            if(currentUser.equals("admin")){
                adminDetails = new AdminDetails(state, userId, userEmailAdress, userName, userImageUrl, userMob);
                iSignInView.goGetAdminDetails(adminDetails);
            }else if(currentUser.equals("user")){
                urlPhoto = String.valueOf(user.getPhotoUrl());
                userDetails =new UserDetails(state, userId, userEmailAdress, userName, userImageUrl, userMob);
                mDatabase.child("user").child(userId).setValue(userDetails);
                iSignInView.storeValuePref(userDetails);
                System.out.println("SignInPresenter updateUI");
                iSignInView.gotLanding();
            }

        } else {
            System.out.println("Something went wrong SignInPresenter updateUI");
        }
    }
/*

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
*/

}
