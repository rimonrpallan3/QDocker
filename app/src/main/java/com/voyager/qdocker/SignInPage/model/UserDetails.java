package com.voyager.qdocker.SignInPage.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rimon on 18-03-2018.
 */

public class UserDetails implements IUserDetials,Parcelable{
    String userId;
    String email;
    String userName;
    String userPhotoUrl;
    Boolean status;
    String usermob;

    public UserDetails(Boolean status,String userId, String email, String userName, String userPhotoUrl, String usermob) {
        this.status =status;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.usermob = usermob;
    }

    protected UserDetails(Parcel in) {
        status = in.readByte() != 0;
        userId = in.readString();
        email = in.readString();
        userName = in.readString();
        userPhotoUrl =in.readString();
        usermob =in.readString();
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

    @Override
    public int describeContents() {
        System.out.println("Describe the content UserDetails");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        System.out.println("writeToParcel to UserDetails ");
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(userId);
        dest.writeString(email);
        dest.writeString(userName);
        dest.writeString(userPhotoUrl);
        dest.writeString(usermob);
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static Creator<UserDetails> getCREATOR() {
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

    @Override
    public void getUserDetials(String userId, String email, String userName, String  userPhotoUrl) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
    }
}
