package com.wraptalk.wraptalk.models;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by jiyoungpark on 15. 10. 21..
 */
public class GameListData {

    private PackageInfo packageInfo;
    private String appName;

    private String user_nick;

    private Drawable appIcon;

    private int flag = 0;

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
}
