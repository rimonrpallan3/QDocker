package com.voyager.qdocker.userViewDoc.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by rimon on 24-03-2018.
 */

public class DocList {
    String docFileName;
    String docFileAbsolutePath;
    ArrayList<File> selectedListDoc;

    public DocList() {
    }

    public DocList(String docFileName, String docFileAbsolutePath, ArrayList<File> selectedListDoc) {
        this.docFileName = docFileName;
        this.docFileAbsolutePath = docFileAbsolutePath;
        this.selectedListDoc = selectedListDoc;
    }

    public String getDocFileName() {
        return docFileName;
    }

    public void setDocFileName(String docFileName) {
        this.docFileName = docFileName;
    }

    public String getDocFileAbsolutePath() {
        return docFileAbsolutePath;
    }

    public void setDocFileAbsolutePath(String docFileAbsolutePath) {
        this.docFileAbsolutePath = docFileAbsolutePath;
    }

    public ArrayList<File> getSelectedListDoc() {
        return selectedListDoc;
    }

    public void setSelectedListDoc(ArrayList<File> selectedListDoc) {
        this.selectedListDoc = selectedListDoc;
    }
}
