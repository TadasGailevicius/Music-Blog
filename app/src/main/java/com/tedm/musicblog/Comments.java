package com.tedm.musicblog;

import java.util.Date;

public class Comments {

    private String message, user_id;
    private Date timestamp;
    private String userimage;

    public Comments(){

    }

    public Comments(String message, String user_id, Date timestamp, String user_image) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.userimage = user_image;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}
