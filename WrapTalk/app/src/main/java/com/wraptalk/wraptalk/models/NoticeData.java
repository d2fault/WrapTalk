package com.wraptalk.wraptalk.models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by jiyoungpark on 15. 10. 31..
 */
public class NoticeData implements Parcelable {

    private String title;
    private String content;
    private String noticeNumber;


    public NoticeData() {
    }

    public NoticeData(Parcel in) {
        readFromParcel(in);
    }

    public NoticeData(String title, String content, String noticeNumber) {
        this.title = title;
        this.content = content;
        this.noticeNumber = noticeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(String notice_no) {
        this.noticeNumber = notice_no;
    }

    private void readFromParcel(Parcel in){
        title = in.readString();
        content = in.readString();
        noticeNumber = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(noticeNumber);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NoticeData createFromParcel(Parcel in) {
            return new NoticeData(in);
        }

        public NoticeData[] newArray(int size) {
            return new NoticeData[size];
        }
    };
}
