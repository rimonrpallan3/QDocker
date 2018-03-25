package com.voyager.qdocker.userViewDoc.presenter;

import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.userViewDoc.view.IUserViewDocView;

import java.util.List;

/**
 * Created by rimon on 24-03-2018.
 */

public class UserViewDocPresenter implements IUserViewDocPresenter {
    IUserViewDocView iUserViewDocView;

    public UserViewDocPresenter(IUserViewDocView iUserViewDocView) {
        this.iUserViewDocView = iUserViewDocView;
    }

    @Override
    public void setUplodLoder(List<String> fileDoneList, int position) {
        iUserViewDocView.hideLoader(fileDoneList,position);
    }

    @Override
    public void generateQrCode(String databseRef, UserDetails userDetails) {
        iUserViewDocView.generateQrCodeActivity(databseRef,userDetails);
    }
}
