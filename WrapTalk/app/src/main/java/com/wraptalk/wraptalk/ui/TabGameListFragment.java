package com.wraptalk.wraptalk.ui;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.adapter.GameListAdapter;
import com.wraptalk.wraptalk.models.GameListData;
import com.wraptalk.wraptalk.utils.DBManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabGameListFragment extends android.support.v4.app.Fragment {

    PackageManager packageManager;
    View view;
    ArrayList<GameListData> source;
    GameListAdapter customAdapter = null;
    ListView listView_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_game_list, container, false);

        initModel();
        getAppInfoByDB();
        initController();
        initView();

        listView_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("num", String.valueOf(position));
                if(source.get(position).getFlag() == 1) {
                    Intent intent = new Intent(getActivity(), ChannelActivity.class);
                    intent.putExtra("packageInfo", source.get(position).getPackageInfo());
                    startActivity(intent);
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) view.findViewById(R.id.listView_gameList);
        packageManager = getActivity().getPackageManager();
    }

    private void initView() {
        customAdapter.notifyDataSetChanged();
    }

    private void initController() {
        customAdapter = new GameListAdapter(getActivity(), source, packageManager);
        listView_result.setAdapter(customAdapter);
    }

    private void getAppInfoByDB() {
        final PackageManager packageManager;
        packageManager = getActivity().getPackageManager();
        DBManager.getInstance().select("SELECT * FROM app_info", new DBManager.OnSelect() {
            @Override
            public void onSelect(Cursor cursor) {
                GameListData data = new GameListData();
                try {
                    data.setPackageInfo(packageManager.getPackageInfo(cursor.getString(cursor.getColumnIndex("app_id")), PackageManager.GET_PERMISSIONS));
                    data.setAppIcon(packageManager.getApplicationIcon(data.getPackageInfo().applicationInfo));
                    data.setAppName(cursor.getString(cursor.getColumnIndex("app_name")));
                    data.setFlag(cursor.getInt(cursor.getColumnIndex("check_registration")));
                    Log.e("flag", String.valueOf(cursor.getInt(cursor.getColumnIndex("check_registration"))));
                    source.add(data);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onErrorHandler(Exception e) {

            }
        });

    }


    public TabGameListFragment() {
        // Required empty public constructor
    }
}
