package com.voyager.qdocker.userViewDoc.view;

import android.content.ClipData;

import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.userViewDoc.model.DocList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rimon on 24-03-2018.
 */

public interface IUserViewDocView {
    /*Used in UserViewDoc Activity*/
    void hideLoader(List<String> fileDoneList, int position);
    void generateQrCodeActivity(String databseRef, UserDetails userDetails);

    /*Used in Adapter*/
    void onItemCheck(DocList docCheckedLists);
    void onItemUncheck(DocList docUnCheckedLists);
}
