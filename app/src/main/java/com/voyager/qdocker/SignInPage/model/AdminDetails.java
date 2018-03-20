package com.voyager.qdocker.SignInPage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 20-Mar-18.
 */

public class AdminDetails implements Parcelable {

    String userId;
    String email;
    String userName;
    String userPhotoUrl;
    Boolean status;
    String usermob;
    String adminId;
    String adminQrCode;

    public AdminDetails(Boolean status, String userId, String email, String userName, String userPhotoUrl, String usermob) {
        this.status = status;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.usermob = usermob;
    }

    protected AdminDetails(Parcel in) {
        status = in.readByte() != 0;
        userId = in.readString();
        email = in.readString();
        userName = in.readString();
        userPhotoUrl = in.readString();
        usermob = in.readString();
        adminId = in.readString();
        adminQrCode = in.readString();
    }

    public static final Creator<AdminDetails> CREATOR = new Creator<AdminDetails>() {
        @Override
        public AdminDetails createFromParcel(Parcel in) {
            return new AdminDetails(in);
        }

        @Override
        public AdminDetails[] newArray(int size) {
            return new AdminDetails[size];
        }
    };

    @Override
    public int describeContents() {
        System.out.println("Describe the content AdminDetails");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        System.out.println("writeToParcel to AdminDetails ");
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(userId);
        dest.writeString(email);
        dest.writeString(userName);
        dest.writeString(userPhotoUrl);
        dest.writeString(usermob);
        dest.writeString(adminId);
        dest.writeString(adminQrCode);
    }

    public Boolean getStatus() {
        return status;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminQrCode() {
        return adminQrCode;
    }

    public void setAdminQrCode(String adminQrCode) {
        this.adminQrCode = adminQrCode;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static Creator<AdminDetails> getCREATOR() {
        return CREATOR;
    }


    public String getUsermob() {
        return usermob;
    }

    public void setUsermob(String usermob) {
        this.usermob = usermob;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
