package com.wraptalk.wraptalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.NoticeData;
import com.wraptalk.wraptalk.utils.NoticeHolder;

import java.util.ArrayList;

/**
 * Created by jiyoungpark on 15. 10. 31..
 */
public class NoticeAdapter extends BaseAdapter{

    private ArrayList<NoticeData> source;
    private LayoutInflater layoutInflater;

    public NoticeAdapter(Context context, ArrayList<NoticeData> source){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<NoticeData> source){

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

        final NoticeData data = (NoticeData) getItem(position);
        final NoticeHolder viewHolder;

        if(convertView == null){

            viewHolder = new NoticeHolder();
            convertView = layoutInflater.inflate(R.layout.layout_notice, parent, false);

            viewHolder.textView_noticeTitle = (TextView) convertView.findViewById(R.id.textView_noticeTitle);
            viewHolder.textView_noticeNumber = (TextView) convertView.findViewById(R.id.textView_noticeNumber);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (NoticeHolder) convertView.getTag();
        }

        viewHolder.textView_noticeTitle.setText(data.getTitle());
        viewHolder.textView_noticeNumber.setText(data.getNoticeNumber());

        return convertView;
    }
}
