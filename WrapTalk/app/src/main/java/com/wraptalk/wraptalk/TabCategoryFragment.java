package com.wraptalk.wraptalk;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabCategoryFragment extends android.support.v4.app.Fragment {


    View view;
    ArrayList<CategoryData> source;
    CategoryAdapter customAdapter = null;
    GridView gridView_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_category, container, false);

        initModel();
        initController();
        initView();

        // Inflate the layout for this fragment
        return view;
    }

    private void initModel() {
        source = new ArrayList<>();
        gridView_result  = (GridView) view.findViewById(R.id.gridView_category);
    }

    private void initView() {
        CategoryData data = new CategoryData();

        data.category = "STUDY";
        source.add(data);

        customAdapter.setSource(source);
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
