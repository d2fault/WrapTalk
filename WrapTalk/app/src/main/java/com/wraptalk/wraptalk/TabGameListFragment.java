package com.wraptalk.wraptalk;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabGameListFragment extends android.support.v4.app.Fragment {

    ArrayList<GameListData> source;

    GameListAdapter customAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initModel();
        initView();
        initController();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_game_list, container, false);
    }

    private void initModel() {

        source = new ArrayList<>();
    }

    private void initView() {

        GameListData data = new GameListData();

        data.gameAppName = "카카오톡";

        customAdapter.setSource(source);


//        customAdapter.notifyDataSetChanged();

    }

    private void initController() {
        customAdapter = new GameListAdapter(getActivity().getApplicationContext(), source);
    }

    public TabGameListFragment() {
        // Required empty public constructor
    }


}
