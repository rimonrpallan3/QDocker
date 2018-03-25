package com.voyager.qdocker.adminLanding.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rimon on 25-03-2018.
 */

public class QrResult implements Parcelable {
    String refDabaseRef;
    String qrUserId;
    String innerChild;

    public QrResult() {
    }

    public QrResult(String refDabaseRef, String qrUserId, String innerChild) {
        this.refDabaseRef = refDabaseRef;
        this.qrUserId = qrUserId;
        this.innerChild = innerChild;
    }

    protected QrResult(Parcel in) {
        refDabaseRef = in.readString();
        qrUserId = in.readString();
        innerChild = in.readString();
    }

    public static final Creator<QrResult> CREATOR = new Creator<QrResult>() {
        @Override
        public QrResult createFromParcel(Parcel in) {
            return new QrResult(in);
        }

        @Override
        public QrResult[] newArray(int size) {
            return new QrResult[size];
        }
    };

    @Override
    public int describeContents() {
        System.out.println("Describe the content QrResult");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        System.out.println("writeToParcel to QrResult ");
        dest.writeString(refDabaseRef);
        dest.writeString(qrUserId);
        dest.writeString(innerChild);
    }

    public String getRefDabaseRef() {
        return refDabaseRef;
    }

    public void setRefDabaseRef(String refDabaseRef) {
        this.refDabaseRef = refDabaseRef;
    }

    public String getQrUserId() {
        return qrUserId;
    }

    public void setQrUserId(String qrUserId) {
        this.qrUserId = qrUserId;
    }

    public String getInnerChild() {
        return innerChild;
    }

    public void setInnerChild(String innerChild) {
        this.innerChild = innerChild;
    }
}
