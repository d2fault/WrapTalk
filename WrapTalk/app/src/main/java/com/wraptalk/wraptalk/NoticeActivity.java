package com.wraptalk.wraptalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
        initView();
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) findViewById(R.id.listVeiw_notice);
    }

    private void initView() {
        NoticeData data = new NoticeData();

        data.setTitle("제목입니다.");
        data.setDate("2015-12-01");

        source.add(data);

        customAdapter.notifyDataSetChanged();
    }

    private void initController() {
        customAdapter = new NoticeAdapter(this, source);
        listView_result.setAdapter(customAdapter);
    }
}
