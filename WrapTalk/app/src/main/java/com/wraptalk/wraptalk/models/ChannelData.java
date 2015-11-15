package com.wraptalk.wraptalk.models;

/**
 * Created by jiyoungpark on 15. 10. 30..
 */
public class ChannelData {
    /*
    channel_name
    public_onoff
    datetime
    channel_limit
    channel_no
    channel_pw
    channel_cate
    default_channel
    channel_id
    app_id
    chief_id
    */

    private String channel_name;
    private String public_onoff;
    private int channel_limit;
    private String channel_cate;
    private String default_channel;
    private String channel_id;
    private String app_id;
    private String chief_id;
    private String user_nick;
    private String user_color;

    private int check_favorite = 0;
    private int check_registeration = 0;

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getPublic_onoff() {
        return public_onoff;
    }

    public void setPublic_onoff(String public_onoff) {
        this.public_onoff = public_onoff;
    }

    public int getChannel_limit() {
        return channel_limit;
    }

    public void setChannel_limit(int channel_limit) {
        this.channel_limit = channel_limit;
    }

    public String getChannel_cate() {
        return channel_cate;
    }

    public void setChannel_cate(String channel_cate) {
        this.channel_cate = channel_cate;
    }

    public String getDefault_channel() {
        return default_channel;
    }

    public void setDefault_channel(String default_channel) {
        this.default_channel = default_channel;
    }

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

    public String getChief_id() {
        return chief_id;
    }

    public void setChief_id(String chief_id) {
        this.chief_id = chief_id;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getUser_color() {
        return user_color;
    }

    public void setUser_color(String user_color) {
        this.user_color = user_color;
    }

    public int getCheck_favorite() {
        return check_favorite;
    }

    public void setCheck_favorite(int check_favorite) {
        this.check_favorite = check_favorite;
    }

    public int getCheck_registeration() {
        return check_registeration;
    }

    public void setCheck_registeration(int check_registeration) {
        this.check_registeration = check_registeration;
    }
}
