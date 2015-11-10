package com.wraptalk.wraptalk.ui;


import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.wraptalk.wraptalk.ui.ChannelActivity;

import java.util.ArrayList;
import java.util.List;


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
        getInstalledApplication();
        initController();
        initView();

        listView_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("num", String.valueOf(position));
                if(source.get(position).isFlag()) {
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

    private void getInstalledApplication() {
        PackageManager packageManager;

        packageManager = getActivity().getPackageManager();
        List<PackageInfo> tempPackageList = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);

        /*To filter out System apps*/
        for(PackageInfo pi : tempPackageList) {
            boolean flag = isSystemPackage(pi);
            if(!flag) {
                GameListData data = new GameListData();
                data.setPackageInfo(pi);
                data.setAppIcon(packageManager.getApplicationIcon(data.packageInfo.applicationInfo));
                data.setAppName(packageManager.getApplicationLabel(data.getPackageInfo().applicationInfo).toString());
                source.add(data);
            }
        }
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    public TabGameListFragment() {
        // Required empty public constructor
    }
}
