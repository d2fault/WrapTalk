package com.wraptalk.wraptalk;

/**
 * Created by jiyoungpark on 15. 11. 1..
 */
public class UserInfo {

    String email;
    String deviceId;
    String gcmKey;
    String token;

    static UserInfo g_userInfo = new UserInfo();

    private UserInfo() {
    }

    public static UserInfo getInstance() {
        return g_userInfo;
    }
}
