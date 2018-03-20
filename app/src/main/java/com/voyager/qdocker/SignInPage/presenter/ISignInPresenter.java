package com.voyager.qdocker.SignInPage.presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by rimon on 18-03-2018.
 */

public interface ISignInPresenter {
    void firebaseAuthWithGoogle(GoogleSignInAccount acct, String currentUser);
    void updateUI(FirebaseUser user,String currentUser);
}
