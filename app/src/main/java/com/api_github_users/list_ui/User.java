package com.api_github_users.list_ui;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private int userId;
    private String userName, avatarUrl, link;

    public User(int userId, String userName, String avatarUrl, String link) {
        this.userId = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.link = link;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getLink() {
        return link;
    }

    private User(Parcel parcel) {
        this.userId = parcel.readInt();
        this.userName = parcel.readString();
        this.avatarUrl = parcel.readString();
        this.link = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(avatarUrl);
        dest.writeString(link);
        dest.writeInt(userId);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}