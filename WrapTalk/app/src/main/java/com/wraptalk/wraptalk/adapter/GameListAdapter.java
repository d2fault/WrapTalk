package com.wraptalk.wraptalk.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.GameListData;
import com.wraptalk.wraptalk.models.UserInfo;
import com.wraptalk.wraptalk.utils.DBManager;
import com.wraptalk.wraptalk.utils.GameListHolder;
import com.wraptalk.wraptalk.utils.OnRequest;
import com.wraptalk.wraptalk.utils.RequestUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by jiyoungpark on 15. 10. 21..
 */
public class GameListAdapter extends BaseAdapter {

    PackageManager packageManager;
    Context context;
    private ArrayList<GameListData> source;
    private LayoutInflater layoutInflater;

    public GameListAdapter(Context context, ArrayList<GameListData> source, PackageManager packageManager){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.source = source;
        this.packageManager = packageManager;

    }

    public void setSource(ArrayList<GameListData> source){

        this.source = source;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (source != null) ? source.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return ((source!=null) && (position >= 0 && position < source.size()) ? source.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final GameListData data = (GameListData) getItem(position);
        final GameListHolder viewHolder;

        if(convertView == null){

            viewHolder = new GameListHolder();
            convertView = layoutInflater.inflate(R.layout.layout_game_list, parent, false);

            viewHolder.imageView_gameAppIcon = (ImageView) convertView.findViewById(R.id.imageView_gameIcon);
            viewHolder.textView_gameAppName = (TextView) convertView.findViewById(R.id.textView_gameTitle);

            viewHolder.imageButton_regist = (ImageButton) convertView.findViewById(R.id.imageButton_regist);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (GameListHolder) convertView.getTag();
        }

        viewHolder.imageView_gameAppIcon.setImageDrawable(data.getAppIcon());
        viewHolder.textView_gameAppName.setText(data.getAppName());

        if(data.getFlag() == 0) {
            viewHolder.imageButton_regist.setBackgroundResource(R.mipmap.ic_plus);
        }
        else {
            viewHolder.imageButton_regist.setBackgroundResource(R.mipmap.ic_minus);
        }

        viewHolder.imageButton_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.getFlag() == 0) {
                    onClickAddButton(viewHolder, data);
                } else {
                    onClickSubButton(viewHolder, data);
                }
            }
        });

        return convertView;
    }

    public void onClickAddButton(final GameListHolder viewHolder, final GameListData data) {
        final View dialogView = layoutInflater.inflate(R.layout.dialog_set_nickname, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText editText = (EditText) dialogView.findViewById(R.id.editText_nickname);

        builder.setIcon(data.getAppIcon());
        builder.setTitle(data.getAppName());
        builder.setView(dialogView);
        dialogView.getTextAlignment();

        TextWatcher watcher = new TextWatcher() {
            String text;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                if(length > 0){
                    Pattern ps = Pattern.compile("^[가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2025a.-zA-Z]*$");//영문, 숫자, 한글만 허용
                    if(!ps.matcher(s).matches()){
                        editText.setText(text);
                        editText.setSelection(editText.length());
                        Log.e("editText", editText.getText().toString());
                    }
                }
            }
        };

        editText.addTextChangedListener(watcher);

        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String url = "http://133.130.113.101:7010/user/registApp?" +
                        "token=" + UserInfo.getInstance().token + "&app_id=" + data.getPackageInfo().applicationInfo.packageName +
                        "&user_nick=";
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(context, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        data.setUser_nick(editText.getText().toString());
                        url += URLEncoder.encode(data.getUser_nick(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    RequestUtil.asyncHttp(url, new OnRequest() {
                        @Override
                        public void onSuccess(String url, byte[] receiveData) {
                            String query = "UPDATE app_info SET check_registration=1,user_nick='" + data.getUser_nick() + "' WHERE app_id='" + data.getPackageInfo().packageName + "'";
                            DBManager.getInstance().write(query);
                        }

                        @Override
                        public void onFail(String url, String error) {
                        }
                    });

                    data.setFlag(1);
                    viewHolder.imageButton_regist.setBackgroundResource(R.mipmap.ic_minus);
                }
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

    public void onClickSubButton(final GameListHolder viewHolder, final GameListData data) {

        final View dialogView = layoutInflater.inflate(R.layout.dialog_sub_application, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setIcon(data.getAppIcon());
        builder.setTitle(data.getAppName());
        builder.setView(dialogView);

        builder.setPositiveButton("SUB", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "http://133.130.113.101:7010/user/removeApp?" +
                        "token=" + UserInfo.getInstance().token + "&app_id=" + data.getPackageInfo().applicationInfo.packageName;

                RequestUtil.asyncHttp(url, new OnRequest() {
                    @Override
                    public void onSuccess(String url, byte[] receiveData) {
                        String query = "UPDATE app_info SET check_registration=0,user_nick='' WHERE app_id='" + data.getPackageInfo().packageName + "';";
                        DBManager.getInstance().write(query);
                        data.setFlag(0);
                        viewHolder.imageButton_regist.setBackgroundResource(R.mipmap.ic_plus);
                    }

                    @Override
                    public void onFail(String url, String error) {

                    }
                });
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
}
