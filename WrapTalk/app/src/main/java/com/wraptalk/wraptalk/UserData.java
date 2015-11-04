package com.wraptalk.wraptalk;

/**
 * Created by jiyoungpark on 15. 11. 1..
 */
public class UserData {

    String email;
    String deviceId;
    String gcmKey;
    String token;

    static UserData g_userData = new UserData();

    private UserData() {
    }

    public static UserData getInstance() {
        return g_userData;
    }
}
