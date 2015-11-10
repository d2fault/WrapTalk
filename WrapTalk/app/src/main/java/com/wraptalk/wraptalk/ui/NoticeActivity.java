package com.wraptalk.wraptalk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wraptalk.wraptalk.adapter.NoticeAdapter;
import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.utils.RequestUtil;
import com.wraptalk.wraptalk.models.NoticeData;
import com.wraptalk.wraptalk.models.UserInfo;
import com.wraptalk.wraptalk.utils.OnRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    View view;
    ArrayList<NoticeData> source;
    NoticeAdapter customAdapter = null;
    ListView listView_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        initModel();
        initController();

        getNoticecList();

        listView_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoticeActivity.this, NoticeContentActivity.class);
                intent.putExtra("notice", source.get(position));
                startActivity(intent);
            }
        });
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) findViewById(R.id.listVeiw_notice);
    }

    private void initController() {
        customAdapter = new NoticeAdapter(this, source);
        listView_result.setAdapter(customAdapter);
    }

    private void getNoticecList() {
        String url = "http://133.130.113.101:7010/user/getNoticeList?" + "token=" + UserInfo.getInstance().token;

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {
                String jsonStr = new String(receiveData);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    int result_code = json.optInt("result_code", -1);

                    if (result_code != 0) {
                        String result_msg = json.optString("result_msg", "fail");
                        Toast.makeText(getApplicationContext(), result_msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray list_notice = json.optJSONArray("list_notice");
                    for (int i = 0; i < list_notice.length(); i++) {
                        JSONObject noticeObj = list_notice.getJSONObject(i);

                        NoticeData data = new NoticeData();

                        data.setNoticeNumber(noticeObj.optString("notice_no"));
                        data.setTitle(noticeObj.optString("subject"));
                        data.setContent(noticeObj.optString("content"));

                        source.add(data);
                    }
                    customAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String url, String error) {

            }
        });
    }
}
