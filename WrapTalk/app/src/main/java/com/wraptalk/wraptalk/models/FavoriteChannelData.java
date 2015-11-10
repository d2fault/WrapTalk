package com.wraptalk.wraptalk.models;

/**
 * Created by jiyoungpark on 15. 11. 3..
 */
public class FavoriteChannelData {

    public String appName;
    public String category;
    public String channelTitle;

    FavoriteChannelData() {

        this.appName = null;
        this.category = null;
        this.channelTitle = null;
    }
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
}
