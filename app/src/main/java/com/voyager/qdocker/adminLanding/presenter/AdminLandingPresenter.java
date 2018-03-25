package com.voyager.qdocker.adminLanding.presenter;

import com.voyager.qdocker.adminLanding.view.IAdminLandingView;

/**
 * Created by rimon on 25-03-2018.
 */

public class AdminLandingPresenter implements IAdminLandingPresenter {

    public IAdminLandingView iAdminLandingView;

    public AdminLandingPresenter(IAdminLandingView iAdminLandingView) {
        this.iAdminLandingView = iAdminLandingView;
    }
}
