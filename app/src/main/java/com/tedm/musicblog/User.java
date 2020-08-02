package com.tedm.musicblog;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String image,name;
    public String email;
    public String user_id;

    public User(){

    }

    public User(String image, String name, String email, String user_id) {
        this.image = image;
        this.name = name;
        this.email = email;
        this.user_id = user_id;
    }

    protected User(Parcel in) {
        image = in.readString();
        name = in.readString();
        email = in.readString();
        user_id = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(name);
    }
}
