package com.voyager.qdocker.adminLanding.view;

import android.widget.ImageButton;

/**
 * Created by rimon on 25-03-2018.
 */

public interface IAdminLandingFrgView {
    void getStoragePermission(String file_url, final String file_name, final ImageButton downloadButton);
}
