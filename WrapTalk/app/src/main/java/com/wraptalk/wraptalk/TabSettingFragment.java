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
public class TabSettingFragment extends android.support.v4.app.Fragment {


    View view;
    ArrayList<SettingData> source;
    ArrayList<String> settingList;
    SettingAdapter customAdapter = null;
    ListView listView_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_setting, container, false);

        initModel();
        initController();
        initView();

        // Inflate the layout for this fragment
        return view;
    }

    private void initModel() {
        source = new ArrayList<>();
        settingList = new ArrayList<>();

        settingList.add("공지사항");
        settingList.add("비밀번호 변경");
        settingList.add("Floating ON/OFF");
        settingList.add("투명도 변경");
        settingList.add("App Version");
        settingList.add("로그아웃");
        settingList.add("회원탈퇴");

        listView_result  = (ListView) view.findViewById(R.id.listVeiw_setting);
    }

    private void initView() {

        for (int i = 0 ; i < settingList.size() ; i++) {
            SettingData data = new SettingData();
            data.setSetting(settingList.get(i));
            source.add(data);
        }
        customAdapter.notifyDataSetChanged();
    }

    private void initController() {
        customAdapter = new SettingAdapter(getActivity().getApplicationContext(), source);
        listView_result.setAdapter(customAdapter);
    }

    public TabSettingFragment() {
        // Required empty public constructor
    }

}
