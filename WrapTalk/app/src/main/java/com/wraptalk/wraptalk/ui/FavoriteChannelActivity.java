package com.wraptalk.wraptalk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.adapter.FavoriteChannelAdapter;
import com.wraptalk.wraptalk.models.FavoriteChannelData;
import com.wraptalk.wraptalk.utils.DBManager;

import java.util.ArrayList;

public class FavoriteChannelActivity extends AppCompatActivity {

    View view;
    ArrayList<FavoriteChannelData> source;
    FavoriteChannelAdapter customAdapter = null;
    ListView listView_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_channel);

        initModel();
        initController();
        getChannelList();
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) findViewById(R.id.listView_favoriteChannel);
    }

    private void initController() {
        customAdapter = new FavoriteChannelAdapter(this, source);
        listView_result.setAdapter(customAdapter);
    }

    private void getChannelList() {
        /* 어떻게 리스트에 넣을지 모르겠음ㅠㅠ */

        final FavoriteChannelData data = new FavoriteChannelData();
        DBManager.getInstance().select("SELECT * FROM chat_info", new DBManager.OnSelect() {
            @Override
            public void onSelect(Cursor cursor) {
                data.setApp_id(cursor.getString(cursor.getColumnIndex("app_id")));
                data.setChannel_id(cursor.getString(cursor.getColumnIndex("channel_id")));
                data.setChannel_name(cursor.getString(cursor.getColumnIndex("channel_name")));
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onErrorHandler(Exception e) {

            }
        });
        source.add(data);
    }
}