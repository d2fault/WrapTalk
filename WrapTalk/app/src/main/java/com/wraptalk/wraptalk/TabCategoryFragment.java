package com.wraptalk.wraptalk;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabCategoryFragment extends android.support.v4.app.Fragment {


    View view;
    ArrayList<CategoryData> source;
    ArrayList<String> categoryList;
    CategoryAdapter customAdapter = null;
    GridView gridView_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_category, container, false);

        initModel();
        initController();
        initView();

        gridView_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegisterCategoryInfo.getInstance().categoryName = categoryList.get(position);
                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                intent.putExtra("categoryName", categoryList.get(position));
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void initModel() {
        source = new ArrayList<>();
        categoryList = new ArrayList<>();

        categoryList.add("Study");
        categoryList.add("Hobby");
        categoryList.add("Meeting");
        categoryList.add("Review");
        categoryList.add("etc");

        gridView_result  = (GridView) view.findViewById(R.id.gridView_category);
    }

    private void initView() {

        for (int i = 0; i < categoryList.size(); i++) {
            CategoryData data = new CategoryData();
            data.setCategory(categoryList.get(i));
            source.add(data);
        }

        customAdapter.notifyDataSetChanged();
    }

    private void initController() {
        customAdapter = new CategoryAdapter(getActivity().getApplicationContext(), source);
        gridView_result.setAdapter(customAdapter);
    }

    public TabCategoryFragment() {
        // Required empty public constructor
    }

}
