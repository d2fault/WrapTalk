package com.wraptalk.wraptalk.models;

/**
 * Created by jiyoungpark on 15. 11. 4..
 */
public class CreateChannelData {
    public String channelName;
    public String channelLimit;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelLimit() {
        return channelLimit;
    }

    public void setChannelLimit(String channelLimit) {
        this.channelLimit = channelLimit;
    }
}
