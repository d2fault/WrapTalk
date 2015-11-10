package com.wraptalk.wraptalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.SettingData;
import com.wraptalk.wraptalk.utils.SettingHolder;

import java.util.ArrayList;

/**
 * Created by jiyoungpark on 15. 10. 23..
 */
public class SettingAdapter extends BaseAdapter {

    private ArrayList<SettingData> source;
    private LayoutInflater layoutInflater;

    public SettingAdapter(Context context, ArrayList<SettingData> source){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<SettingData> source){

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

        SettingData data = (SettingData) getItem(position);
        SettingHolder viewHolder;

        if (convertView == null) {
            viewHolder = new SettingHolder();

            if (position == 3) {
                convertView = layoutInflater.inflate(R.layout.layout_setting_switch, parent, false);
            } else if (position == 4) {
                convertView = layoutInflater.inflate(R.layout.layout_setting_version, parent, false);
            } else {
                convertView = layoutInflater.inflate(R.layout.layout_setting, parent, false);
            }

            viewHolder.textView_setting = (TextView) convertView.findViewById(R.id.textView_setting);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SettingHolder) convertView.getTag();
        }

        viewHolder.textView_setting.setText(data.setting);


        return convertView;
    }
}

