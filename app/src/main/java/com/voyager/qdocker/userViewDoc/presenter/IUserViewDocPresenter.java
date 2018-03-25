package com.voyager.qdocker.userViewDoc.presenter;

import com.voyager.qdocker.SignInPage.model.UserDetails;

import java.util.List;

/**
 * Created by rimon on 24-03-2018.
 */

public interface IUserViewDocPresenter {
    void setUplodLoder(List<String> fileDoneList,final int position);
    void generateQrCode(String databseRef, UserDetails userDetails);
}
