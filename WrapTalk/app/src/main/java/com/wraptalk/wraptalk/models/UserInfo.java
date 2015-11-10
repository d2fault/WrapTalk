package com.wraptalk.wraptalk.models;

/**
 * Created by jiyoungpark on 15. 11. 1..
 */
public class UserInfo {

    public String email;
    public String deviceId;
    public String gcmKey;
    public String token;

    static UserInfo g_userInfo = new UserInfo();

    private UserInfo() {
    }

    public static UserInfo getInstance() {
        return g_userInfo;
    }
}
