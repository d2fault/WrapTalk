package com.wraptalk.wraptalk.ui;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.adapter.CategoryAdapter;
import com.wraptalk.wraptalk.models.CategoryData;
import com.wraptalk.wraptalk.models.UserInfo;
import com.wraptalk.wraptalk.utils.AppSetting;
import com.wraptalk.wraptalk.utils.DBManager;
import com.wraptalk.wraptalk.utils.OnRequest;
import com.wraptalk.wraptalk.utils.RequestUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabCategoryFragment extends android.support.v4.app.Fragment {

    public static final String PRE_CHANNEL_PREFIX = "category.";

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
                if (source.get(position).getCheck_registration() == 1) {
                    Intent intent = new Intent(getActivity(), ChannelActivity.class);
                    intent.putExtra("categoryName", PRE_CHANNEL_PREFIX + categoryList.get(position));
                    startActivity(intent);
                } else {
                    onClickCategory(position);
                }
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
            final CategoryData data = new CategoryData();
            data.setApp_name(categoryList.get(i));
            data.setApp_id(PRE_CHANNEL_PREFIX + categoryList.get(i));

            String query = String.format("INSERT INTO app_info (app_id, app_name, user_nick, check_registration) VALUES ('%s', '%s', '%s', %d)",
                    data.getApp_id(), data.getApp_name(), data.getUser_nick(), data.getCheck_registration());
            try {
                DBManager.getInstance().write(query);
            } catch (Exception e) {

            }

            DBManager.getInstance().select("SELECT * FROM app_info", new DBManager.OnSelect() {
                @Override
                public void onSelect(Cursor cursor) {
                    data.setCheck_registration(0);

                    if (cursor.getInt(cursor.getColumnIndex("check_registration")) == 1) {
                        data.setCheck_registration(1);
                        cursor.close();
                    }
                }

                @Override
                public void onComplete(int cnt) {

                }

                @Override
                public void onErrorHandler(Exception e) {

                }
            });

            source.add(data);
        }

        customAdapter.notifyDataSetChanged();
    }

    private void initController() {
        customAdapter = new CategoryAdapter(getActivity().getApplicationContext(), source);
        gridView_result.setAdapter(customAdapter);
    }

    public void onClickCategory(final int position) {
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_set_nickname, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(source.get(position).getApp_name());
        builder.setView(dialogView);
        dialogView.getTextAlignment();

        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = (EditText) dialogView.findViewById(R.id.editText_nickname);
                String url = AppSetting.REST_URL + "/user/registApp?" +
                        "token=" + UserInfo.getInstance().token + "&app_id=" + source.get(position).getApp_id() +
                        "&user_nick=";
                try {
                    source.get(position).setUser_nick(editText.getText().toString());
                    Log.e("edittext nickname", editText.getText().toString());
                    url += URLEncoder.encode(source.get(position).getUser_nick(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                RequestUtil.asyncHttp(url, new OnRequest() {
                    @Override
                    public void onSuccess(String url, byte[] receiveData) {
                        String query = "UPDATE app_info SET check_registration=1,user_nick='" + source.get(position).getUser_nick() +
                                "' WHERE app_id='" + source.get(position).getApp_id() + "';";
                        DBManager.getInstance().write(query);
                        Toast.makeText(getActivity(), "카테고리 닉네임이 성공적으로 등록되었습니다.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFail(String url, String error) {
                        Toast.makeText(getActivity(), "카테고리 닉네임 등록에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                });
                Log.e("nickname", source.get(position).getUser_nick());
                source.get(position).setCheck_registration(1);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //설정한 값으로 AlertDialog 객체 생성
        AlertDialog dialog = builder.create();

        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정

        //Dialog 보이기
        dialog.show();
    }

    public TabCategoryFragment() {
        // Required empty public constructor
    }
}
