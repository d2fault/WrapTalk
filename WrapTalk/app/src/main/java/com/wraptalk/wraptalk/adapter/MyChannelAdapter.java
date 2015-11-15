package com.wraptalk.wraptalk.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.MyChannelData;
import com.wraptalk.wraptalk.utils.MyChannelHolder;

import java.util.ArrayList;

/**
 * Created by jiyoungpark on 15. 10. 23..
 */
public class MyChannelAdapter extends BaseAdapter{

    private ArrayList<MyChannelData> source;
    private LayoutInflater layoutInflater;
    private PackageManager packageManager;

    public MyChannelAdapter(Context context, ArrayList<MyChannelData> source){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        packageManager = context.getPackageManager();

        this.source = source;
    }

    public void setSource(ArrayList<MyChannelData> source){

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
    public View getView(int position, View convertView, ViewGroup parent) {

        MyChannelData data = (MyChannelData) getItem(position);
        MyChannelHolder viewHolder;

        if(convertView == null){

            viewHolder = new MyChannelHolder();
            convertView = layoutInflater.inflate(R.layout.layout_my_channel, parent, false);

            viewHolder.textView_myChannelTtitle = (TextView) convertView.findViewById(R.id.textView_myChannelTitle);
            viewHolder.textView_myNickname = (TextView) convertView.findViewById(R.id.textView_myNickname);
            viewHolder.textView_countUnreadMessage = (TextView) convertView.findViewById(R.id.textView_countUnreadMessage);
            viewHolder.imageView_appIcon = (ImageView) convertView.findViewById(R.id.imageView_appIcon);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (MyChannelHolder) convertView.getTag();
        }

        viewHolder.textView_myChannelTtitle.setText(data.getChannel_name());
        viewHolder.textView_myNickname.setText(data.getUser_nick());
        viewHolder.textView_countUnreadMessage.setText("0");

        try {
            viewHolder.imageView_appIcon.setImageDrawable(packageManager.getApplicationIcon(data.getApp_id()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
