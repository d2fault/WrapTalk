package com.wraptalk.wraptalk;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity {

    View view;
    ArrayList<ChannelData> source;
    ChannelAdapter customAdapter = null;
    ListView listView_result;

    EditText editText_searchChannel;
    Button button_search;

    PackageManager packageManager;
    PackageInfo packageInfo;
    String searchKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        Intent intent = getIntent();
        packageInfo = (PackageInfo) intent.getExtras().get("packageInfo");

        getSupportActionBar().setTitle(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());


        initModel();
        initNetwork();
        initController();

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeyword = editText_searchChannel.getText().toString();
                channelSearch();
            }
        });
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) findViewById(R.id.listVeiw_channel);
        editText_searchChannel = (EditText) findViewById(R.id.editText_searchChannel);
        button_search = (Button) findViewById(R.id.button_search);
    }

    private void initController() {
        customAdapter = new ChannelAdapter(this, source);
        listView_result.setAdapter(customAdapter);
    }

    private void initNetwork() {
        String url = "http://133.130.113.101:7010/user/appChannel?" +
                "token=" + UserData.getInstance().token + "&app_id=" + packageInfo.packageName;

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {
                String jsonStr = new String(receiveData);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    int result_code = json.optInt("result_code", -1);

                    if(result_code != 0) {
                        String result_msg = json.optString("result_msg", "fail");
                        Toast.makeText(getApplicationContext(), result_msg, Toast.LENGTH_SHORT).show();

                        return;
                    }

                    JSONArray list_channel = json.optJSONArray("list_channel");
                    for (int i = 0 ; i < list_channel.length() ; i++) {
                        JSONObject channelObj = list_channel.getJSONObject(i);

                        ChannelData data = new ChannelData();

                        data.setChannelName(channelObj.optString("channel_name"));
                        data.setuserCount(channelObj.optString("user_count"));

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

    private void channelSearch() {
        String url = "http://133.130.113.101:7010/user/searchChannel?" +
                "token=" + UserData.getInstance().token + "&keyword=" + searchKeyword + "&app_id=" + packageInfo.packageName;

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {
                String jsonStr = new String(receiveData);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    int result_code = json.optInt("result_code", -1);

                    if(result_code != 0) {
                        String result_msg = json.optString("result_msg", "fail");
                        Toast.makeText(getApplicationContext(), result_msg, Toast.LENGTH_SHORT).show();

                        return;
                    }

                    JSONArray list_channel = json.optJSONArray("list_channel");
                    for (int i = 0 ; i < list_channel.length() ; i++) {
                        JSONObject channelObj = list_channel.getJSONObject(i);

                        ChannelData data = new ChannelData();

                        data.setChannelName(channelObj.optString("channel_name"));
                        data.setuserCount(channelObj.optString("user_count"));

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
