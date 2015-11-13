package com.wraptalk.wraptalk.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.adapter.ChannelAdapter;
import com.wraptalk.wraptalk.models.ChannelData;
import com.wraptalk.wraptalk.models.CreateChannelData;
import com.wraptalk.wraptalk.models.UserInfo;
import com.wraptalk.wraptalk.utils.DBManager;
import com.wraptalk.wraptalk.utils.OnRequest;
import com.wraptalk.wraptalk.utils.RequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity {

    ArrayList<ChannelData> source;
    ChannelAdapter customAdapter = null;
    ListView listView_result;

    EditText editText_searchChannel;
    Button button_search;

    PackageInfo packageInfo;
    String categoryName;
    String searchKeyword;

    CreateChannelData channelData;
    String url;

    String app_id, channel_title, master_id, user_color, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        Intent intent = getIntent();
        packageInfo = (PackageInfo) intent.getExtras().get("packageInfo");
        categoryName = intent.getStringExtra("categoryName");

        initModel();
        getChannelList();
        initController();

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeyword = editText_searchChannel.getText().toString();
                searchChannel();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_changeNickname :
                showChangeNickDialog();
                break;
            case R.id.action_createChannel :
                showCreateChannelDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) findViewById(R.id.listVeiw_channel);
        editText_searchChannel = (EditText) findViewById(R.id.editText_searchChannel);
        button_search = (Button) findViewById(R.id.button_search);
        if(categoryName != null && categoryName.startsWith(TabCategoryFragment.PRE_CHANNEL_PREFIX)) {
            app_id = categoryName;
        }
        else {
            app_id = packageInfo.packageName;
        }

        getNickname();
    }

    private void initController() {
        customAdapter = new ChannelAdapter(this, source);
        listView_result.setAdapter(customAdapter);
    }

    private void showChangeNickDialog() {

        // Diaolog 생성
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_set_nickname, null);
        PackageManager packageManager = this.getPackageManager();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText_nickname = (EditText) dialogView.findViewById(R.id.editText_nickname);

        builder.setView(dialogView);
        builder.setTitle(app_id);

        url = "http://133.130.113.101:7010/user/registNick?token=" + UserInfo.getInstance().token + "&app_id=" + app_id;

        if(categoryName != null) {
            builder.setIcon(packageManager.getApplicationIcon(packageInfo.applicationInfo));
        }

        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                url += "&user_nick=";
                try {
                    url += URLEncoder.encode(editText_nickname.getText().toString(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                RequestUtil.asyncHttp(url, new OnRequest() {
                    @Override
                    public void onSuccess(String url, byte[] receiveData) {
                        Toast.makeText(ChannelActivity.this, "닉네임 변경에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String url, String error) {
                        Toast.makeText(ChannelActivity.this, "변경 실패 : 중복된 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //설정한 값으로 AlertDialog 객체 생성
        AlertDialog dialog = builder.create();

        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정

        //Dialog 보이기
        dialog.show();
    }

    private void showCreateChannelDialog() {

        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_channel, null);

        final CheckBox checkBox_channelOnoff = (CheckBox) dialogView.findViewById(R.id.checkBox_channelOnoff);
        final EditText editText_setPassword = (EditText) dialogView.findViewById(R.id.editText_setPassword);
        final TextView textView_password = (TextView) dialogView.findViewById(R.id.textView_password);

        textView_password.setVisibility(View.INVISIBLE);
        editText_setPassword.setVisibility(View.INVISIBLE);

        checkBox_channelOnoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    textView_password.setVisibility(View.INVISIBLE);
                    editText_setPassword.setVisibility(View.INVISIBLE);
                }
                else {
                    textView_password.setVisibility(View.VISIBLE);
                    editText_setPassword.setVisibility(View.VISIBLE);
                }
            }
        });


        final Spinner spinner_userCount = (Spinner) dialogView.findViewById(R.id.spinner_userCount);
        ArrayAdapter<CharSequence> adapter;

        ArrayList<CharSequence> list = new ArrayList<>();
        for(int i = 1; i < 101 ; i++)
            list.add(String.valueOf(i));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_userCount.setAdapter(adapter); // OK!!

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("채널 생성");
        builder.setView(dialogView);

        spinner_userCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                channelData = new CreateChannelData();
                channelData.setChannelLimit(String.valueOf(position + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ChannelActivity.this, "인원을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getNewChannelInfo(dialogView);

                RequestUtil.asyncHttp(url, new OnRequest() {
                    @Override
                    public void onSuccess(String url, byte[] receiveData) {

                        String jsonStr = new String(receiveData);
                        String query;
                        try {

                            final ChannelData data = new ChannelData();
                            JSONObject json = new JSONObject(jsonStr);

                            data.setChannel_id(json.optString("channel_id"));
                            data.setPublic_onoff(json.optString("public_onoff"));
                            data.setChannel_limit(json.optInt("channel_limit"));
                            data.setChannel_cate(json.optString("channel_cate"));
                            data.setApp_id(json.optString("app_id"));
                            data.setChannel_name(json.optString("channel_name"));
                            // userNick은 app_info에서 갖다가 써야 함
                            data.setUser_color(json.optString("user_color"));
                            data.setChief_id(json.optString("chief_id"));
                            data.setFlag(1);

                            query = String.format( "INSERT INTO chat_info " +
                                            "(channel_id, public_onoff, channel_limit, channel_cate, app_id," +
                                            "channel_name, user_nick, chief_id, user_color) " +
                                            "VALUES ('%s', '%s', '%d', '%s', '%s', '%s', '%s', '%s', '%s')",
                                    json.optString("channel_id"), json.optString("public_onoff"), json.optInt("channel_limit"),
                                    json.optString("channel_cate"), json.optString("app_id"), json.optString("channel_name"),
                                    nickname, UserInfo.getInstance().email, "#FFFFFF");

                            DBManager.getInstance().write(query);
                            source.add(data);
                            customAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(ChannelActivity.this, "채널 생성에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                        getChannelList();
                        customAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(String url, String error) {
                        Toast.makeText(ChannelActivity.this, "채널 생성에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //설정한 값으로 AlertDialog 객체 생성
        AlertDialog dialog = builder.create();

        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정

        //Dialog 보이기
        dialog.show();
    }

    private void getNewChannelInfo(View v) {

        final EditText editText_channelName = (EditText) v.findViewById(R.id.editText_channelName);
        final EditText editText_setPassword = (EditText) v.findViewById(R.id.editText_setPassword);
        final CheckBox checkBox_channelOnoff = (CheckBox) v.findViewById(R.id.checkBox_channelOnoff);


        channelData.setChannelName(editText_channelName.getText().toString());
        /* url 생성 */

        url = "http://133.130.113.101:7010/user/makeChannel?token=" + UserInfo.getInstance().token + "&app_id=" + app_id;

        try {
            url += "&channel_name=" + URLEncoder.encode(channelData.getChannelName(), "utf-8") + "&public_onoff=";
            channel_title = channelData.getChannelName();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(checkBox_channelOnoff.isChecked()) {
            url += "on";
        }
        else {
            url += "off&channel_pw=" + editText_setPassword.getText().toString();
        }

        master_id = UserInfo.getInstance().email;
        user_color = "#000000";
        url += "&channel_limit=" + channelData.getChannelLimit() + "&user_nick=" + nickname;

    }

    private void getChannelList() {

        url = "http://133.130.113.101:7010/user/appChannel?" + "token=" + UserInfo.getInstance().token + "&app_id=" + app_id;

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

                    JSONArray list_channel = json.optJSONArray("list_channel");
                    for (int i = 0; i < list_channel.length(); i++) {
                        JSONObject channelObj = list_channel.getJSONObject(i);

                        final ChannelData data = new ChannelData();

                        data.setChannel_id(channelObj.optString("channel_id"));
                        data.setPublic_onoff(channelObj.optString("public_onoff"));
                        data.setChannel_limit(channelObj.optInt("channel_limit"));
                        data.setChannel_cate(channelObj.optString("channel_cate"));
                        data.setApp_id(channelObj.optString("app_id"));
                        data.setChannel_name(channelObj.optString("channel_name"));
                        // userNick은 app_info에서 갖다가 써야 함
                        data.setUser_color(channelObj.optString("user_color"));
                        data.setChief_id(channelObj.optString("chief_id"));

                        String query = "SELECT * FROM chat_info WHERE app_id='" + app_id + "'";

                        DBManager.getInstance().select(query, new DBManager.OnSelect() {
                            @Override
                            public void onSelect(Cursor cursor) {
                                String id1 = data.getChannel_id();
                                String id2 = cursor.getString(cursor.getColumnIndex("channel_id"));
                                Log.e("onSelect", id1 + "," + id2);

                                if (id1.equals(id2)) {
                                    data.setFlag(1);
                                } else if (data.getFlag() != 1) {
                                    data.setFlag(0);
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.e("onComplete", "o");
                            }

                            @Override
                            public void onErrorHandler(Exception e) {

                            }
                        });
                        Log.e("Flag", String.valueOf(data.getFlag()));
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

    private void searchChannel() {

        String url = "http://133.130.113.101:7010/user/appChannel?" + "token=" + UserInfo.getInstance().token + "&channel_name=" + searchKeyword + "&app_id=" + app_id;

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {
                String jsonStr = new String(receiveData);
                source.removeAll(source);

                try {
                    JSONObject json = new JSONObject(jsonStr);
                    int result_code = json.optInt("result_code", -1);

                    if (result_code != 0) {
                        String result_msg = json.optString("result_msg", "fail");
                        Toast.makeText(getApplicationContext(), result_msg, Toast.LENGTH_SHORT).show();

                        return;
                    }

                    JSONArray list_channel = json.optJSONArray("list_channel");
                    for (int i = 0; i < list_channel.length(); i++) {
                        JSONObject channelObj = list_channel.getJSONObject(i);

                        final ChannelData data = new ChannelData();

                        data.setChannel_id(channelObj.optString("channel_id"));
                        data.setPublic_onoff(channelObj.optString("public_onoff"));
                        data.setChannel_limit(channelObj.optInt("channel_limit"));
                        data.setChannel_cate(channelObj.optString("channel_cate"));
                        data.setApp_id(channelObj.optString("app_id"));
                        data.setChannel_name(channelObj.optString("channel_name"));
                        // userNick은 app_info에서 갖다가 써야 함
                        // user_color는 chat에서
                        data.setChief_id(channelObj.optString("chief_id"));

                        DBManager.getInstance().select("SELECT * FROM chat_info WHERE app_id='" + app_id + "'", new DBManager.OnSelect() {
                            @Override
                            public void onSelect(Cursor cursor) {
                                cursor.moveToFirst();
                                while (!cursor.isLast()) {
                                    if (cursor.getString(cursor.getColumnIndex("channel_id")).equals(data.getChannel_id())) {
                                        data.setFlag(1);
                                    } else {
                                        data.setFlag(0);
                                    }
                                }
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

    private void getNickname() {
        DBManager.getInstance().select("SELECT * FROM app_info WHERE app_id='" + app_id + "'", new DBManager.OnSelect() {
            @Override
            public void onSelect(Cursor cursor) {
                cursor.moveToFirst();
                nickname = cursor.getString(cursor.getColumnIndex("user_nick"));
                Log.e("nickname", nickname);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onErrorHandler(Exception e) {
            }
        });
    }

    /* 등록하려고 액티비티 띄울 때
       채널 리스트를 받아온다.
       채널 리스트를 받아옴과 동시에
       DB에 있는 내용 : SELECT * FROM chat_info WHERE app_id == \' " + app_id + "\'" -> 이렇게 선택하고
       channel list와 비교한다.
       if(cursor.getColumnIndex("channel_id").equal(data.getChannel_id)) <- 이런 식으로
       { data.setflag(1) }
       else {
        data.setflag(0)
       }
     */
}
