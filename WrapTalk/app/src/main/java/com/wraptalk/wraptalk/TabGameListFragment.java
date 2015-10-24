package com.wraptalk.wraptalk;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabGameListFragment extends android.support.v4.app.Fragment {

    View view;
    ArrayList<GameListData> source;
    GameListAdapter customAdapter = null;
    ListView listView_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_game_list, container, false);

        initModel();
        initController();
        initView();

        // Inflate the layout for this fragment
        return view;
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) view.findViewById(R.id.listView_gameList);
    }

    private void initView() {
        GameListData data = new GameListData();

        data.gameAppName = "카카오톡";
        source.add(data);

        customAdapter.notifyDataSetChanged();
    }

    private void initController() {
        customAdapter = new GameListAdapter(getActivity().getApplicationContext(), source);
        listView_result.setAdapter(customAdapter);
    }

    public TabGameListFragment() {
        // Required empty public constructor
    }
}
