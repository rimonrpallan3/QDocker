package com.voyager.qdocker.adminAddDetails.presenter;

import com.voyager.qdocker.adminAddDetails.view.IAdminAddView;

/**
 * Created by User on 20-Mar-18.
 */

public class AdminAddPresenter implements IAdminAddPresenter {
    IAdminAddView iAdminAddView;
    public AdminAddPresenter(IAdminAddView iAdminAddView) {
        this.iAdminAddView = iAdminAddView;
    }

    @Override
    public void storeMoveToLanding(String adminId, String adminQrCode) {

    }
}
