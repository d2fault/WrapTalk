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

    private String channelName;
    private String channelOnoff;
    private String channelLimit;
    private String channelId;
    private String appId;

    Boolean flag = false;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelOnoff() {
        return channelOnoff;
    }

    public void setChannelOnoff(String channelOnoff) {
        this.channelOnoff = channelOnoff;
    }

    public String getChannelLimit() {
        return channelLimit;
    }

    public void setChannelLimit(String channelLimit) {
        this.channelLimit = channelLimit;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
