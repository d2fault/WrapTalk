package com.wraptalk.wraptalk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wraptalk.wraptalk.R;

import java.util.ArrayList;

/**
 * Created by lk on 15. 10. 30..
 */
public class ChattingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> chatData;

    public ChattingAdapter(Context context, ArrayList<String> chatData){
        this.context = context;
        this.chatData = chatData;
    }

    @Override
    public int getCount() {
        return chatData.size();
    }

    @Override
    public Object getItem(int position) {
        return chatData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.chatting_message_2, parent, false);
        }
        TextView nickname = (TextView)row.findViewById(R.id.tv_chatting_nickname);
        TextView chatrow = (TextView)row.findViewById(R.id.tv_chatting_chatdata);
        TextView chatDate = (TextView)row.findViewById(R.id.tv_chatting_date);

        String[] data = chatData.get(position).split("/&");
        SpannableStringBuilder sp = null;
        if("normal".equals(data[0])) {
            String [] nick = data[1].split("&&");
            int color = Integer.parseInt(nick[1]);
            sp = new SpannableStringBuilder(nick[0]);
            sp.setSpan(new ForegroundColorSpan(Color.rgb(Color.red(color), Color.green(color), Color.blue(color))), 0, nick[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nickname.setTypeface(null, Typeface.NORMAL);
            nickname.setText(sp);
            nickname.append("   ");
            chatrow.setText(data[2]);
            chatDate.setText(data[3]);
        }else if("notice".equals(data[0])){
            sp = new SpannableStringBuilder("공지사항");
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#FF00FF")), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nickname.setTypeface(null, Typeface.BOLD);
            nickname.setText(sp);
            nickname.append(" ");
            chatrow.setText(data[2]);
            chatDate.setText(data[3]);
        }else if("log".equals(data[0])) {

            sp = new SpannableStringBuilder(data[1]);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#FF00FF")), 0, data[1].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nickname.setTypeface(null, Typeface.BOLD);
            nickname.setText(sp);
            nickname.append("   ");
            sp = new SpannableStringBuilder(data[1] + data[2]);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#FF00FF")), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            chatrow.setTypeface(null, Typeface.BOLD);
            chatrow.setText(sp);
            chatDate.setText(data[3]);
        }else{
            chatrow.setTypeface(null, Typeface.NORMAL);
            chatrow.setText("ERROR");
            chatrow.append(" ");
            nickname.setText("ERROR");
            chatDate.setText("ERROR");
        }
        return row;
    }
}
