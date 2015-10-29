package com.wraptalk.wraptalk;


import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

        // Inflate the layout for this fragment
        return view;
    }

    private void initModel() {
        source = new ArrayList<>();
        listView_result  = (ListView) view.findViewById(R.id.listView_gameList);
        packageManager = getActivity().getPackageManager();
    }

    private void initView() {
        GameListData data = new GameListData();
        //source.add(data);

        customAdapter.notifyDataSetChanged();
    }

    private void initController() {
        customAdapter = new GameListAdapter(getActivity().getApplicationContext(), source, packageManager);
        listView_result.setAdapter(customAdapter);
    }

    private void getInstalledApplication() {
        PackageManager packageManager;

        packageManager = getActivity().getPackageManager();
        List<PackageInfo> packageList = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);

        List<PackageInfo> packageList1 = new ArrayList<PackageInfo>();

        /*To filter out System apps*/
        for(PackageInfo pi : packageList) {
            GameListData data = new GameListData();
            boolean flag = isSystemPackage(pi);
            if(!flag) {
                packageList1.add(pi);
                data.setPackageInfo(pi);
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
