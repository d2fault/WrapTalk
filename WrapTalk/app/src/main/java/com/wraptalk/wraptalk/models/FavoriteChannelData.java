package com.wraptalk.wraptalk.models;

/**
 * Created by jiyoungpark on 15. 11. 3..
 */
public class FavoriteChannelData {

    private String app_id;
    private String channel_id;
    private String channel_name;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }
}
