package com.wraptalk.wraptalk;

/**
 * Created by jiyoungpark on 15. 10. 30..
 */
public class ChannelData {

    String channelName;
    String masterNickname;
    Boolean flag = false;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMasterNickname() {
        return masterNickname;
    }

    public void setMasterNickname(String masterNickname) {
        this.masterNickname = masterNickname;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
