package com.wraptalk.wraptalk.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.wraptalk.wraptalk.adapter.ChannelAdapter;
import com.wraptalk.wraptalk.models.ChannelData;
import com.wraptalk.wraptalk.models.CreateChannelData;
import com.wraptalk.wraptalk.utils.OnRequest;
import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.utils.RequestUtil;
import com.wraptalk.wraptalk.models.UserInfo;

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

        url = "http://133.130.113.101:7010/user/registNick?token=" + UserInfo.getInstance().token + "&app_id=";

        if(categoryName != null && categoryName.startsWith(TabCategoryFragment.PRE_CHANNEL_PREFIX)) {
            builder.setTitle(categoryName);
            url += categoryName;//
        }
        else {

            builder.setIcon(packageManager.getApplicationIcon(packageInfo.applicationInfo));
            builder.setTitle(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
            url += packageInfo.packageName;
        }

        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                url += "&nick=";

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

                getChannelInfo(dialogView);

                RequestUtil.asyncHttp(url, new OnRequest() {
                    @Override
                    public void onSuccess(String url, byte[] receiveData) {
                        /* 바뀐 것 바로 반영하고 싶은데... */
                        Toast.makeText(ChannelActivity.this, "채널 생성에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
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

    private void getChannelInfo(View v) {

        final EditText editText_channelName = (EditText) v.findViewById(R.id.editText_channelName);
        final EditText editText_setPassword = (EditText) v.findViewById(R.id.editText_setPassword);
        final CheckBox checkBox_channelOnoff = (CheckBox) v.findViewById(R.id.checkBox_channelOnoff);


        channelData.setChannelName(editText_channelName.getText().toString());
        /* url 생성 */

        url = "http://133.130.113.101:7010/user/makeChannel?token=" + UserInfo.getInstance().token + "&app_id=";

        if(categoryName != null && categoryName.startsWith(TabCategoryFragment.PRE_CHANNEL_PREFIX)) {
            url += categoryName;
        }
        else {
            url += packageInfo.packageName;
        }

        try {
            url += "&channel_name=" + URLEncoder.encode(channelData.getChannelName(), "utf-8") + "&public_onoff=";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(checkBox_channelOnoff.isChecked()) {
            url += "on";
        }
        else {
            url += "off&channel_pw=" + editText_setPassword.getText().toString();
        }

        url += "&channel_limit=" + channelData.getChannelLimit() + "&user_nick=" + "임시닉네임";
    }

    private void getChannelList() {

        url = "http://133.130.113.101:7010/user/appChannel?" + "token=" + UserInfo.getInstance().token + "&app_id=";

        if(categoryName != null && categoryName.startsWith(TabCategoryFragment.PRE_CHANNEL_PREFIX)) {
            url += categoryName;//RegisterCategoryInfo.getInstance().categoryName;
        }
        else {
            url += packageInfo.packageName;
        }

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

                        ChannelData data = new ChannelData();

                        data.setChannelName(channelObj.optString("channel_name"));
                        data.setChannelOnoff(channelObj.optString("public_onoff"));
                        data.setChannelLimit(channelObj.optString("channel_limit"));
                        data.setChannelId(channelObj.optString("channel_id"));

                        if(categoryName != null && categoryName.startsWith(TabCategoryFragment.PRE_CHANNEL_PREFIX)) {
                            data.setAppId(categoryName);
                        }
                        else {
                            data.setAppId(packageInfo.packageName);
                        }

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

        String url = "http://133.130.113.101:7010/user/appChannel?" + "token=" + UserInfo.getInstance().token + "&channel_name=" + searchKeyword + "&app_id=";


        if(categoryName != null && categoryName.startsWith(TabCategoryFragment.PRE_CHANNEL_PREFIX)) {
            url += categoryName;
        }
        else {
            url += packageInfo.packageName;
        }

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {
                String jsonStr = new String(receiveData);
                source.removeAll(source);

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
                        data.setChannelOnoff(channelObj.optString("public_onoff"));
                        data.setChannelLimit(channelObj.optString("channel_limit"));
                        data.setChannelId(channelObj.optString("channel_id"));

                        if(packageInfo != null) {
                            data.setAppId(packageInfo.packageName);
                        }
                        else {
                            data.setAppId(categoryName);
                        }
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
