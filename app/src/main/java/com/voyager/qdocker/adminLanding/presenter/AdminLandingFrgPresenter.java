package com.voyager.qdocker.adminLanding.presenter;

import android.widget.ImageButton;

import com.voyager.qdocker.adminLanding.view.IAdminLandingFrgView;

/**
 * Created by rimon on 25-03-2018.
 */

public class AdminLandingFrgPresenter implements IAdminLandingFrgPresenter{

    IAdminLandingFrgView iAdminLandingFrgView;

    public AdminLandingFrgPresenter(IAdminLandingFrgView iAdminLandingFrgView) {
        this.iAdminLandingFrgView =iAdminLandingFrgView;
    }

    @Override
    public void methodRequiresTwoPermission(String file_url, final String file_name, final ImageButton downloadButton) {
        iAdminLandingFrgView.getStoragePermission(file_url,file_name,downloadButton);
    }
}
