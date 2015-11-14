package com.wraptalk.wraptalk.models;

/**
 * Created by jiyoungpark on 15. 10. 21..
 */
public class CategoryData {

    private String app_id;
    private String app_name;
    private String user_nick;
    private int check_registration = 0;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public int getCheck_registration() {
        return check_registration;
    }

    public void setCheck_registration(int check_registration) {
        this.check_registration = check_registration;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }
}
