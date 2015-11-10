package com.wraptalk.wraptalk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wraptalk.wraptalk.R;

public class ChattingActivity extends AppCompatActivity {

    ImageButton imageButton_ring;
    ImageButton imageButton_setting;
    Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Intent intent = getIntent();
        String title = intent.getStringExtra("channelName"); // bell on off 유무도 받아야 한다.

        Log.e("title", title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        imageButton_ring = (ImageButton) findViewById(R.id.imageButton_ring);
        imageButton_setting = (ImageButton) findViewById(R.id.imageButton_setting);

        imageButton_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag) {
                    imageButton_ring.setBackgroundResource(R.mipmap.ic_bell_off);
                    flag = false;
                }
                else {
                    imageButton_ring.setBackgroundResource(R.mipmap.ic_bell_on);
                    flag = true;
                }
            }
        });

        imageButton_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Set", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
