package com.voyager.qdocker.SplashScreen.view;

import com.voyager.qdocker.SignInPage.model.AdminDetails;
import com.voyager.qdocker.SignInPage.model.UserDetails;

/**
 * Created by User on 8/28/2017.
 */

public interface ISplashView {
    void moveToSignUpLogin();
    void moveToUserLanding(UserDetails userDetails);
    void moveToAdminLanding(AdminDetails adminDetails);
}
