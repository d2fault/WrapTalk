package com.wraptalk.wraptalk.models;

/**
 * Created by jiyoungpark on 15. 10. 23..
 */
public class MyChannelData {

    private String myChannelTitle;
    private String myNickname;
    private int countUnreadMessage;

    public String getMyChannelTitle() {
        return myChannelTitle;
    }

    public void setMyChannelTitle(String myChannelTitle) {
        this.myChannelTitle = myChannelTitle;
    }

    public String getMyNickname() {
        return myNickname;
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
    }

    public int getCountUnreadMessage() {
        return countUnreadMessage;
    }

    public void setCountUnreadMessage(int countUnreadMessage) {
        this.countUnreadMessage = countUnreadMessage;
    }
}
