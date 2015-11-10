package com.wraptalk.wraptalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.CategoryData;
import com.wraptalk.wraptalk.utils.CategoryHolder;

import java.util.ArrayList;

/**
 * Created by jiyoungpark on 15. 10. 21..
 */
public class CategoryAdapter extends BaseAdapter {

    private ArrayList<CategoryData> source;
    private LayoutInflater layoutInflater;

    public CategoryAdapter(Context context, ArrayList<CategoryData> source){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<CategoryData> source){

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

        CategoryData data = (CategoryData) getItem(position);
        CategoryHolder viewHolder;

        if(convertView == null){

            viewHolder = new CategoryHolder();
            convertView = layoutInflater.inflate(R.layout.layout_category, parent, false);

            viewHolder.textView_category = (TextView) convertView.findViewById(R.id.textView_category);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (CategoryHolder) convertView.getTag();
        }

        viewHolder.textView_category.setText(data.category);

        return convertView;
    }
}
