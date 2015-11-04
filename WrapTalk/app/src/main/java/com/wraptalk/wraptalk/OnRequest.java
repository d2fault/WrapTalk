package com.wraptalk.wraptalk;

/**
 * Created by jiyoungpark on 15. 11. 3..
 */
public interface OnRequest {
    public void onSuccess(String url, byte[] receiveData);
    public void onFail(String url, String error);
}
