package com.voyager.qdocker.userViewDoc.view;

import android.content.ClipData;

import com.voyager.qdocker.userViewDoc.model.DocList;

import java.util.ArrayList;

/**
 * Created by rimon on 24-03-2018.
 */

public interface IUserViewDocView {
    /*Used in Adapter*/
    void onItemCheck(DocList docCheckedLists);
    void onItemUncheck(DocList docUnCheckedLists);
}
