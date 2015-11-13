package com.wraptalk.wraptalk.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.ChannelData;
import com.wraptalk.wraptalk.models.UserInfo;
import com.wraptalk.wraptalk.utils.ChannelHolder;
import com.wraptalk.wraptalk.utils.DBManager;
import com.wraptalk.wraptalk.utils.OnRequest;
import com.wraptalk.wraptalk.utils.RequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import android.support.v7.app.AlertDialog;

/**
 * Created by jiyoungpark on 15. 10. 30..
 */
public class ChannelAdapter extends BaseAdapter{

    Context context;
    private ArrayList<ChannelData> source;
    private LayoutInflater layoutInflater;
    String query = "";
    String nickname;

    public ChannelAdapter(Context context, ArrayList<ChannelData> source){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.source = source;
    }

    public void setSource(ArrayList<ChannelData> source){

        this.source = source;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (source != null) ? source.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return ((source!=null) && (position >= 0 && position < source.size()) ? source.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ChannelData data = (ChannelData) getItem(position);
        final ChannelHolder viewHolder;

        if(convertView == null){

            viewHolder = new ChannelHolder();
            convertView = layoutInflater.inflate(R.layout.layout_channel, parent, false);

            viewHolder.textView_channelTitle = (TextView) convertView.findViewById(R.id.textView_channelTitle);
            viewHolder.textView_channelOnoff = (TextView) convertView.findViewById(R.id.textView_channelOnoff);

            viewHolder.button_enter = (ImageButton) convertView.findViewById(R.id.button_enter);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ChannelHolder) convertView.getTag();
        }

        if(data.getFlag() == 0) {
            viewHolder.button_enter.setBackgroundResource(R.mipmap.ic_plus);
        }
        else {
            viewHolder.button_enter.setBackgroundResource(R.mipmap.ic_minus);
        }
        getNickname(data);

        viewHolder.textView_channelTitle.setText(data.getChannel_name());
        viewHolder.textView_channelOnoff.setText(data.getPublic_onoff());

        viewHolder.button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.getFlag() == 0) {
                    onClickJoinButton(viewHolder, data);
                } else {
                    onClickQuitButton(viewHolder, data);
                }
            }
        });

        return convertView;
    }

    private void onClickJoinButton(final ChannelHolder viewHolder, final ChannelData data) {
        //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
        //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언

        final View dialogView = layoutInflater.inflate(R.layout.dialog_join_channel, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);

        builder.setPositiveButton("JOIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                joinChannel(data);
                data.setFlag(1);
                viewHolder.button_enter.setBackgroundResource(R.mipmap.ic_minus);
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

    private void onClickQuitButton(final ChannelHolder viewHolder, final ChannelData data) {
        //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
        //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언

        final View dialogView = layoutInflater.inflate(R.layout.dialog_quit_channel, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);

        builder.setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quitChannel(data);
                data.setFlag(0);
                viewHolder.button_enter.setBackgroundResource(R.mipmap.ic_plus);
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

    private void joinChannel(final ChannelData data) {

        String url = "http://133.130.113.101:7010/user/joinChannel?token=" + UserInfo.getInstance().token + "&channel_id=" + data.getChannel_id() +
                "&user_nick=" + nickname;
        if (data.getPublic_onoff().equals("off")) {
            url += "&channel_pw=" + "1234"; // 현재는 임시. 나중에는 dialog 띄워서 받아야함
        }
        url += "&app_id=" + data.getApp_id();

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {
                String jsonStr = new String(receiveData);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    JSONArray list_channel = json.optJSONArray("list_channel");
                    for (int i = 0; i < list_channel.length(); i++) {
                        JSONObject channelObj = list_channel.getJSONObject(i);
                        query = String.format("INSERT INTO chat_info " +
                                        "(channel_id, public_onoff, channel_limit, channel_cate, app_id," +
                                        "channel_name, chief_id, user_color) " +
                                        "VALUES ('%s', '%s', '%d', '%s', '%s', '%s', '%s', '%s')",
                                channelObj.optString("channel_id"), channelObj.optString("public_onoff"), channelObj.optInt("channel_limit"),
                                channelObj.optString("channel_cate"), channelObj.optString("app_id"), channelObj.optString("channel_name"),
                                UserInfo.getInstance().email, "#FFFFFF");
                        Log.e("query", query);
                        DBManager.getInstance().write(query);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String url, String error) {

            }
        });
    }

    private void quitChannel(final ChannelData data) {
        String url = "http://133.130.113.101:7010/user/withdrawChannel?token=" + UserInfo.getInstance().token + "&channel_id=" + data.getChannel_id();

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {

                query = "DELETE FROM chat_info WHERE channel_id=" + data.getChannel_id();
            }

            @Override
            public void onFail(String url, String error) {

            }
        });
    }

    private void getNickname(ChannelData data) {
        DBManager.getInstance().select("SELECT * FROM app_info WHERE app_id='" + data.getApp_id() + "'", new DBManager.OnSelect() {
            @Override
            public void onSelect(Cursor cursor) {
                nickname = cursor.getString(cursor.getColumnIndex("user_nick"));
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onErrorHandler(Exception e) {
            }
        });
    }
}
