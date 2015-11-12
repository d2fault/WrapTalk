package com.wraptalk.wraptalk.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wraptalk.wraptalk.utils.DBManager;
import com.wraptalk.wraptalk.utils.OnRequest;
import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.utils.RequestUtil;
import com.wraptalk.wraptalk.adapter.MyChannelAdapter;
import com.wraptalk.wraptalk.models.MyChannelData;
import com.wraptalk.wraptalk.models.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabMyChannelFragment extends android.support.v4.app.Fragment {

    View view;
    ArrayList<MyChannelData> source;
    MyChannelAdapter customAdapter = null;
    ListView listView_result = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_my_channel, container, false);

        //((MainActivity) getActivity()).getSupportActionBar().setTitle("c");

        initModel();
        initController();
        getMyChannelList();

        listView_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                intent.putExtra("channelName", source.get(position).getChannel_name());
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result = (ListView) view.findViewById(R.id.listView_myChannel);
    }

    public void getMyChannelList() {

        String url = "http://133.130.113.101:7010/user/listChannel?" + "token=" + UserInfo.getInstance().token;

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {
                String jsonStr = new String(receiveData);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    int result_code = json.optInt("result_code", -1);

                    if (result_code != 0) {
                        String result_msg = json.optString("result_msg", "fail");
                        return;
                    }

                    JSONArray list_channel = json.optJSONArray("list_channel");
                    for (int i = 0; i < list_channel.length(); i++) {
                        JSONObject channelObj = list_channel.getJSONObject(i);

                        MyChannelData data = new MyChannelData();

                        data.setChannel_id(channelObj.optString("channel_id"));
                        data.setApp_id(channelObj.optString("app_id"));
                        data.setChannel_name(channelObj.optString("channel_name"));
                        data.setUser_nick(channelObj.optString("user_nick"));
                        data.setChief_id(channelObj.optString("chief_id"));
                        data.setUser_color(channelObj.optString("user_color"));

                        source.add(data);

                        String query = String.format( "INSERT INTO chat_info (" +
                                        "channel_id, app_id, channel_name, user_nick, chief_id, user_color) VALUES ('%s', '%s', '%s', '%s', '%s', %s)",
                                source.get(i).getChannel_id(), source.get(i).getApp_id(), source.get(i).getChannel_name(),
                                source.get(i).getUser_nick(), source.get(i).getChief_id(), source.get(i).getUser_color());

                        DBManager.getInstance().write(query);
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

    private void insertDB(int position) {


    }
    private void initController() {
        customAdapter = new MyChannelAdapter(getActivity().getApplicationContext(), source);
        listView_result.setAdapter(customAdapter);
    }

    public TabMyChannelFragment() {
        // Required empty public constructor
    }


}
