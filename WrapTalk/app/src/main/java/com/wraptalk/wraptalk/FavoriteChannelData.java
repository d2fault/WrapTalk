package com.wraptalk.wraptalk;

/**
 * Created by jiyoungpark on 15. 11. 3..
 */
public class FavoriteChannelData {

    String appName;
    String category;
    String channelTitle;

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
