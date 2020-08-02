package com.tedm.musicblog;

import java.util.Date;

public class TaskList {
    private String user_image;
    private String blog_desc;
    private String blog_image_thumb;
    private String blog_image_url;
    private String blog_post_id;
    private String blog_song_duration;
    private String blog_song_id;
    private String blog_song_link;
    private String blog_song_title;
    private String blog_user_id;
    private int percentage;
    private String task;
    private String task_type;
    private String task_user;
    private String user_id;

    public TaskList(){}

    public TaskList(String user_image, String blog_desc, String blog_image_thumb, String blog_image_url, String blog_post_id, String blog_song_duration, String blog_song_id, String blog_song_link, String blog_song_title, String blog_user_id, int percentage, String task, String task_type, String task_user, String user_id) {
        this.user_image = user_image;
        this.blog_desc = blog_desc;
        this.blog_image_thumb = blog_image_thumb;
        this.blog_image_url = blog_image_url;
        this.blog_post_id = blog_post_id;
        this.blog_song_duration = blog_song_duration;
        this.blog_song_id = blog_song_id;
        this.blog_song_link = blog_song_link;
        this.blog_song_title = blog_song_title;
        this.blog_user_id = blog_user_id;
        this.percentage = percentage;
        this.task = task;
        this.task_type = task_type;
        this.task_user = task_user;
        this.user_id = user_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getBlog_desc() {
        return blog_desc;
    }

    public void setBlog_desc(String blog_desc) {
        this.blog_desc = blog_desc;
    }

    public String getBlog_image_thumb() {
        return blog_image_thumb;
    }

    public void setBlog_image_thumb(String blog_image_thumb) {
        this.blog_image_thumb = blog_image_thumb;
    }

    public String getBlog_image_url() {
        return blog_image_url;
    }

    public void setBlog_image_url(String blog_image_url) {
        this.blog_image_url = blog_image_url;
    }

    public String getBlog_post_id() {
        return blog_post_id;
    }

    public void setBlog_post_id(String blog_post_id) {
        this.blog_post_id = blog_post_id;
    }

    public String getBlog_song_duration() {
        return blog_song_duration;
    }

    public void setBlog_song_duration(String blog_song_duration) {
        this.blog_song_duration = blog_song_duration;
    }

    public String getBlog_song_id() {
        return blog_song_id;
    }

    public void setBlog_song_id(String blog_song_id) {
        this.blog_song_id = blog_song_id;
    }

    public String getBlog_song_link() {
        return blog_song_link;
    }

    public void setBlog_song_link(String blog_song_link) {
        this.blog_song_link = blog_song_link;
    }

    public String getBlog_song_title() {
        return blog_song_title;
    }

    public void setBlog_song_title(String blog_song_title) {
        this.blog_song_title = blog_song_title;
    }

    public String getBlog_user_id() {
        return blog_user_id;
    }

    public void setBlog_user_id(String blog_user_id) {
        this.blog_user_id = blog_user_id;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getTask_user() {
        return task_user;
    }

    public void setTask_user(String task_user) {
        this.task_user = task_user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
