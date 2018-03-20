package com.voyager.qdocker.AdminUserDetails.presenter;

import com.voyager.qdocker.AdminUserDetails.view.IAdminView;

/**
 * Created by User on 20-Mar-18.
 */

public class AdminPresenter implements IAdminPresenter {
    IAdminView iAdminView;
    public AdminPresenter(IAdminView iAdminView) {
        this.iAdminView = iAdminView;
    }

    @Override
    public void storeMoveToLanding(String adminId, String adminQrCode) {

    }
}
