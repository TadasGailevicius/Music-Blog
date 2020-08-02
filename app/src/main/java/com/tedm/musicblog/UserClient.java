package com.tedm.musicblog;

import android.app.Application;

import com.tedm.musicblog.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
