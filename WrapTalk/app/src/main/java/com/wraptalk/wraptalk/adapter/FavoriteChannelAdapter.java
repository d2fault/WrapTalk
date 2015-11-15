package com.wraptalk.wraptalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.FavoriteChannelData;
import com.wraptalk.wraptalk.utils.FavoriteChannelHolder;

import java.util.ArrayList;

/**
 * Created by jiyoungpark on 15. 11. 3..
 */
public class FavoriteChannelAdapter extends BaseAdapter {

    private ArrayList<FavoriteChannelData> source;
    private LayoutInflater layoutInflater;
    RadioGroup group;

    public FavoriteChannelAdapter(Context context, ArrayList<FavoriteChannelData> source){
        group = new RadioGroup(context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<FavoriteChannelData> source){

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

        final FavoriteChannelData data = (FavoriteChannelData) getItem(position);
        final FavoriteChannelHolder viewHolder;

        if(convertView == null){

            viewHolder = new FavoriteChannelHolder();
            convertView = layoutInflater.inflate(R.layout.layout_favorite_channel, parent, false);

            viewHolder.textView_channelName = (TextView) convertView.findViewById(R.id.textView_channelName);
            viewHolder.radioButton_star = (RadioButton) convertView.findViewById(R.id.radioButton_star);

            group.addView(viewHolder.radioButton_star);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (FavoriteChannelHolder) convertView.getTag();
        }

        viewHolder.textView_channelName.setText(data.getChannel_name());
        //viewHolder.textView_noticeNumber.setText(data.getNoticeNumber());

        viewHolder.radioButton_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(layoutInflater.getContext(), "즐겨찾기에 등록되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
