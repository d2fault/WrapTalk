package com.wraptalk.wraptalk.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wraptalk.wraptalk.utils.OnRequest;
import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.utils.RequestUtil;
import com.wraptalk.wraptalk.models.ChannelData;
import com.wraptalk.wraptalk.models.UserInfo;
import com.wraptalk.wraptalk.utils.ChannelHolder;

import java.util.ArrayList;

//import android.support.v7.app.AlertDialog;

/**
 * Created by jiyoungpark on 15. 10. 30..
 */
public class ChannelAdapter extends BaseAdapter{

    Context context;
    private ArrayList<ChannelData> source;
    private LayoutInflater layoutInflater;

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

        viewHolder.textView_channelTitle.setText(data.getChannelName());
        viewHolder.textView_channelOnoff.setText(data.getChannelOnoff());

        viewHolder.button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!data.getFlag()) {
                    onClickJoinButton(viewHolder, data);
                } else {
                    onClickQuitButton(viewHolder, data);
                }
                Log.e("listener", String.valueOf(position));
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
                data.setFlag(true);
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
                data.setFlag(false);
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

    private void joinChannel(ChannelData data) {
        String url = "http://133.130.113.101:7010/user/joinChannel?token=" + UserInfo.getInstance().token + "&channel_id=" + data.getChannelId() +
                "&user_nick=임시닉네임";

        if (data.getChannelOnoff().equals("off")) {
            url += "&channel_pw=" + "1234"; // 현재는 임시. 나중에는 dialog 띄워서 받아야함
        }

        url += "&app_id=" + data.getAppId();

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {

            }

            @Override
            public void onFail(String url, String error) {

            }
        });
    }

    private void quitChannel(ChannelData data) {
        String url = "http://133.130.113.101:7010/user/withdrawChannel?token=" + UserInfo.getInstance().token + "&channel_id=" + data.getChannelId();

        RequestUtil.asyncHttp(url, new OnRequest() {
            @Override
            public void onSuccess(String url, byte[] receiveData) {

            }

            @Override
            public void onFail(String url, String error) {

            }
        });
    }
}
