package com.wraptalk.wraptalk.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wraptalk.wraptalk.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lk on 15. 11. 17..
 */
public class ChatSelectDialog extends DialogFragment {


    ListView mLocationList;
    private ArrayList<String> mOfficeListItems = new ArrayList<String>();
    private Context context;

    public ChatSelectDialog (Context context, ArrayList<String> list) {
        Bundle args = new Bundle();
        this.context = context;
        this.mOfficeListItems = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.chatlist, container,
                true);

        mLocationList = (ListView)v.findViewById(R.id.lv_chatlist_chatlist);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_list_item_1, mOfficeListItems);
        mLocationList.setAdapter(adapter);

        getDialog().setTitle("채팅방을 선택하세요.");

        return v;
    }


}