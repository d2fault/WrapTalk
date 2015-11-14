package com.wraptalk.wraptalk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.adapter.ChattingAdapter;
import com.wraptalk.wraptalk.services.SockJSImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChattingActivity extends AppCompatActivity {

    private ImageButton imageButton_ring;
    private ImageButton imageButton_setting;
    private Boolean flag = true;
    private ListView list;
    private ChattingAdapter adapter;
    private ArrayList<String> chatdata;
    private EditText mEditText;
    private String channel_id;
    private SockJSImpl sockJS;
    private int nickColor;
    private ColorPicker cp;
    private Button colorButton;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Intent intent = getIntent();
        String title = intent.getStringExtra("channelName"); // bell on off 유무도 받아야 한다.
        channel_id = intent.getStringExtra("channel_id");
        nickname = intent.getStringExtra("nickname");

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

        list = (ListView) findViewById(R.id.lv_chatting_list);
        chatdata = new ArrayList<>();
        adapter = new ChattingAdapter(getApplicationContext(), chatdata);
        list.setAdapter(adapter);

        cp = new ColorPicker(getApplication(), 255,255,255);

        mEditText = (EditText) findViewById(R.id.et_chatting_chat);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
                                       @Override
                                       public boolean onKey(View v, int keyCode, KeyEvent event) {

                                           Log.i("keycode", keyCode + "");
                                           if (event.getAction() != KeyEvent.ACTION_DOWN)
                                               return true;


                                           if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                               Log.d("Send", "KeyEvent.KEYCODE_ENTER");
                                               JSONObject obj = send();
                                               if ("".equals(mEditText.getText().toString()))
                                                   return true;
                                               sockJS.send(obj);
                                               Log.i("fff", "send event");
                                               mEditText.setText("");
                                               return true;
                                           } else if (mEditText.getText().toString().contains("\n")) {
                                               JSONObject obj = send();
                                               if ("".equals(mEditText.getText().toString()))
                                                   return true;
                                               sockJS.send(obj);
                                               mEditText.setText("");
                                               return true;
                                           } else if(keyCode == 4){
                                               finish();
                                           }

                                           return false;
                                       }
                                   }

        );
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("text", s + "");
                if (s.toString().contains("\n")) {
                    mEditText.setText(mEditText.getText().toString().replace("\n", ""));
                    JSONObject obj = send();
                    if ("".equals(mEditText.getText().toString()))
                        return;
                    Log.i("json", obj.toString());
                    sockJS.send(obj);
                    mEditText.setText("");
                }
                Log.i("Enter ", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        colorButton = (Button) findViewById(R.id.bt_chatting_color);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cp.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                //cp.show();
                cp.show();

                Button okButton = (Button) cp.findViewById(R.id.okColorButton);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nickColor = Color.rgb(cp.getRed(), cp.getGreen(), cp.getBlue());
                        colorButton.setBackgroundColor(nickColor);
                        cp.dismiss();
                    }
                });
            }
        });

        connectSockJS();

    }

    @NonNull
    private JSONObject send() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "publish");
            obj.put("address", "to.server.channel");
            JSONObject body = new JSONObject();
            body.put("type", "normal");
            body.put("channel_id", channel_id);
            body.put("sender_id", "aaa");
            body.put("sender_nick", nickname+"&&" + nickColor);
            body.put("app_id", "com.aaa.aaa");
            body.put("msg", mEditText.getText().toString());
            obj.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("onClick", e.toString());
        }
        return obj;
    }

    private void connectSockJS() {
        try {
            sockJS = new SockJSImpl("http://133.130.113.101:7030/eventbus", channel_id, nickname) {
                //channel_
                @Override
                public void parseSockJS(String s) {
                    try {
                        //System.out.println(s);
                        s = s.replace("\\\"", "\"");
                        s = s.replace("\\\\", "\\");
//                        s = s.replace("\\\\\"", "\"");
                        s = s.substring(3, s.length() - 2); // a[" ~ "] 없애기
                        Log.i("Reci", s);

                        JSONObject json = new JSONObject(s);
                        String type = json.getString("type");
                        String address = json.getString("address");
//                        final JSONObject body = json.getJSONObject("body");
                        final JSONObject body = new JSONObject(json.getString("body"));
                        String bodyType = body.getString("type");
                        String msg = body.getString("msg");
                        String nickname = body.getString("sender_nick");
                        Date myDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
                        String date = sdf.format(myDate);
                        final String data =  bodyType + "/&" +nickname + "/&" + msg + "/&" + date;
                        if (("to.channel."+channel_id).equals(address))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatdata.add(data);
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        System.out.println("body = " + body);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            boolean b = sockJS.connectBlocking();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
