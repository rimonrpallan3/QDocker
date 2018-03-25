package com.voyager.qdocker.userViewDoc.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rimon on 25-03-2018.
 */

public class UploadDocs {

    String fileNmae;
    String url;

    public UploadDocs() {
    }

    public UploadDocs( String fileNmae, String url) {
        this.fileNmae = fileNmae;
        this.url = url;
    }

    public String getFileNmae() {
        return fileNmae;
    }

    public void setFileNmae(String fileNmae) {
        this.fileNmae = fileNmae;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
