package com.wraptalk.wraptalk;

/**
 * Created by jiyoungpark on 15. 10. 30..
 */
public class ChannelData {

    String channelName;
    String channelOnoff;
    Boolean flag = false;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getchannelOnoff() {
        return channelOnoff;
    }

    public void setchannelOnoff(String userCount) {
        this.channelOnoff = userCount;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
