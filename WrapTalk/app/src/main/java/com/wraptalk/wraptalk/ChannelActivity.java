package com.wraptalk.wraptalk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity {

    public static ChannelActivity thisObj;
    View view;
    ArrayList<ChannelData> source;
    ChannelAdapter customAdapter = null;
    ListView listView_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        thisObj = this;
        initModel();
        initController();
        initView();
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) findViewById(R.id.listVeiw_channel);
    }

    private void initView() {
        ChannelData data = new ChannelData();

        data.channelName = "channel Name";
        data.masterNickname = "who";

        source.add(data);

        customAdapter.notifyDataSetChanged();
    }

    private void initController() {
        customAdapter = new ChannelAdapter(this, source);
        listView_result.setAdapter(customAdapter);
    }
}
