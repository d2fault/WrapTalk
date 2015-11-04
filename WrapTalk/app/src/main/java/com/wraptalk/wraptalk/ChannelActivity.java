package com.wraptalk.wraptalk;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

    PackageInfo packageInfo;
    String searchKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        Intent intent = getIntent();
        packageInfo = (PackageInfo) intent.getExtras().get("packageInfo");

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
                setNickname();
                break;
            case R.id.action_createChannel :
                createChannel();
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

    private void setNickname() {

        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_set_nickname, null);
        PackageManager packageManager = this.getPackageManager();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(packageInfo != null) {
            builder.setIcon(packageManager.getApplicationIcon(packageInfo.applicationInfo));
            builder.setTitle(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
        }
        else {
            builder.setTitle(RegisterCategoryInfo.getInstance().categoryName);
        }

        builder.setView(dialogView);

        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
    private void createChannel() {

        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_channel, null);
        Spinner spinner;
        ArrayAdapter<CharSequence> adapter;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        spinner = (Spinner) dialogView.findViewById(R.id.spinner_userCount);

        ArrayList<CharSequence> list = new ArrayList<>();
        for(int i = 1; i < 101 ; i++)
            list.add(String.valueOf(i));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter); // OK!!

        builder.setTitle("채널 생성");
        builder.setView(dialogView);


        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

    private void getChannelList() {

        String url = "http://133.130.113.101:7010/user/appChannel?" + "token=" + UserInfo.getInstance().token + "&app_id=";

        if(packageInfo != null) {
            url += packageInfo.packageName;
        }
        else {
            url += RegisterCategoryInfo.getInstance().categoryName;
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

    private void searchChannel() {

        String url = "http://133.130.113.101:7010/user/appChannel?" + "token=" + UserInfo.getInstance().token + "&keyword=" + searchKeyword + "&app_id=";

        if(packageInfo != null) {
            url += packageInfo.packageName;
        }
        else {
            url += RegisterCategoryInfo.getInstance().categoryName;
        }

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
