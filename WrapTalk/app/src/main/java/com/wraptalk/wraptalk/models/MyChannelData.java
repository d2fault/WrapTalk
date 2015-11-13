package com.wraptalk.wraptalk.models;

/**
 * Created by jiyoungpark on 15. 10. 23..
 */
public class MyChannelData {

    private String channel_id;
    private String app_id;
    private String channel_name;
    private String user_nick;
    private String chief_id;
    private String user_color;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getChief_id() {
        return chief_id;
    }

    public void setChief_id(String chief_id) {
        this.chief_id = chief_id;
    }

    public String getUser_color() {
        return user_color;
    }

    public void setUser_color(String user_color) {
        this.user_color = user_color;
    }
}
