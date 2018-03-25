package com.voyager.qdocker.adminLanding.presenter;

import android.widget.ImageButton;

/**
 * Created by rimon on 25-03-2018.
 */

public interface IAdminLandingFrgPresenter {
    void methodRequiresTwoPermission(String file_url, final String file_name, final ImageButton downloadButton);
}
