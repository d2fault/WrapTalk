package com.wraptalk.wraptalk.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jiyoungpark on 15. 11. 3..
 */
public class RequestUtil extends AsyncTask<Void, Void, byte[]> {

    String url;
    OnRequest on;

    private RequestUtil(String url, OnRequest on) {
        this.url = url;
        this.on = on;
    }

    public static void asyncHttp(String url, OnRequest on) {

        RequestUtil req = new RequestUtil(url, on);
        req.execute();
    }

    protected static byte[] requestHttp(String urlStr) throws IOException {

        URL url = new URL(urlStr);
        Log.e("서버에 보내는 내용", url.toString());

        // 전송하는 부분

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
        conn.setDoOutput(true);
        // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
        conn.setDoInput(true);

        try (
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream(40960);
        ) {
            // 데이터 받는 부분
            byte tmpRead[] = new byte[10240];

            while (true) {
                int nRead = is.read(tmpRead);
                if (nRead == -1)
                    break;
                baos.write(tmpRead, 0, nRead);
            }

            byte readData[] = baos.toByteArray();
            String strData = new String(readData);
            JSONObject json = new JSONObject(strData);
            int result_code = json.optInt("result_code", -1);

            Log.e("서버에서 받은 내용", strData);

            if (result_code != 0) {
                return null;
            }

            return readData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected byte[] doInBackground(Void... params) {
        try {
            return requestHttp(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(byte[] data) {
        if(data != null)
            on.onSuccess(url, data);
        else
            on.onFail(url, null);
    }
}
