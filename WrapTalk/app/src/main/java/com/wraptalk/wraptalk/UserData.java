package com.wraptalk.wraptalk;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jiyoungpark on 15. 11. 1..
 */
public class UserData implements Parcelable {

    String email;
    String deviceId;
    String gcmKey;

    protected UserData(Parcel in) {
        email = in.readString();
        deviceId = in.readString();
        gcmKey = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public UserData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(deviceId);
        dest.writeString(gcmKey);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getGcmKey() {
        return gcmKey;
    }

    public void setGcmKey(String gcmKey) {
        this.gcmKey = gcmKey;
    }

}
