package com.wraptalk.wraptalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jiyoungpark on 15. 10. 21..
 */
public class GameListAdapter extends BaseAdapter {

    private ArrayList<GameListData> source;
    private LayoutInflater layoutInflater;

    public GameListAdapter(Context context, ArrayList<GameListData> source){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;

    }

    public void setSource(ArrayList<GameListData> source){

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

        GameListData data = (GameListData) getItem(position);
        GameListHolder viewHolder;

        if(convertView == null){

            viewHolder = new GameListHolder();
            convertView = layoutInflater.inflate(R.layout.layout_game_list, parent, false);

            viewHolder.textView_gameAppName = (TextView) convertView.findViewById(R.id.textView_gameTitle);
            //viewHolder.imageView_gameAppIcon = (ImageView) convertView.findViewById(R.id.imageView_gameIcon);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (GameListHolder) convertView.getTag();
        }

        viewHolder.textView_gameAppName.setText(data.gameAppName);

        return convertView;
    }
}
