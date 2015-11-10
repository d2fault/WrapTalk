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

    public boolean flag = false;

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
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
