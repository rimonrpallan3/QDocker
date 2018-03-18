package com.voyager.qdocker.SignInPage.view;

import com.voyager.qdocker.SignInPage.model.UserDetails;

/**
 * Created by rimon on 18-03-2018.
 */

public interface ISignInView {
    void setLoader(int visibility);
    void storeValuePref(UserDetails userDetails);
    void gotLanding();
}
