package com.wraptalk.wraptalk.models;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by jiyoungpark on 15. 10. 21..
 */
public class GameListData {

    public PackageInfo packageInfo;
    public String appName;
    public Drawable appIcon;

    public Integer flag = 0;

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

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
    public int isFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
}
