package com.voyager.qdocker.SignInPage.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.IUserDetials;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.SignInPage.view.ISignInView;

/**
 * Created by rimon on 18-03-2018.
 */

public class SignInPresenter implements ISignInPresenter{

    ISignInView iSignInView;
    private static final String TAG = "SignInPresenter";
    FirebaseAuth mAuth;
    Activity activity;
    IUserDetials iUserDetials;
    UserDetails userDetails;
    String urlPhoto;


    public SignInPresenter(ISignInView iSignInView,FirebaseAuth mAuth,Activity activity) {
        this.iSignInView = iSignInView;
        this.mAuth = mAuth;
        this.activity = activity;
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
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
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
                            updateUI(user);
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
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        iSignInView.setLoader(View.GONE);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
    @Override
    public void updateUI(FirebaseUser user) {
        iSignInView.setLoader(View.GONE);
        if (user != null) {
            urlPhoto = String.valueOf(user.getPhotoUrl());
            userDetails =new UserDetails(user.getUid(),user.getEmail(),user.getDisplayName(),urlPhoto);
            iSignInView.storeValuePref(userDetails);
            iSignInView.gotLanding();
        } else {
            System.out.println("Something went wrong SignInPresenter updateUI");
        }
    }

    /*private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                    }
                });
    }*/

}
