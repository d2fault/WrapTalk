package com.wraptalk.wraptalk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.NoticeData;

public class NoticeContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_content);

        TextView textView_title = (TextView) findViewById(R.id.textView_title);
        TextView textView_content = (TextView) findViewById(R.id.textView_content);
        NoticeData data;
        Intent intent = getIntent();

        data = intent.getParcelableExtra("notice");

        textView_title.setText(data.getTitle());
        textView_content.setText(data.getContent());
    }
}