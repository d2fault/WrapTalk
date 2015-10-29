package com.wraptalk.wraptalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
            data.setAppName(packageManager.getApplicationLabel(data.getPackageInfo().applicationInfo).toString());
            data.setAppIcon(packageManager.getApplicationIcon(data.packageInfo.applicationInfo));

            viewHolder = new GameListHolder();
            convertView = layoutInflater.inflate(R.layout.layout_game_list, parent, false);

            viewHolder.textView_gameAppName = (TextView) convertView.findViewById(R.id.textView_gameTitle);
            viewHolder.imageView_gameAppIcon = (ImageView) convertView.findViewById(R.id.imageView_gameIcon);

            viewHolder.button_regist = (Button) convertView.findViewById(R.id.button_regist);

            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (GameListHolder) convertView.getTag();
        }

        viewHolder.textView_gameAppName.setText(data.getAppName());
        viewHolder.imageView_gameAppIcon.setImageDrawable(data.appIcon);

        viewHolder.button_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!data.flag) {
                    onClickAddButton(viewHolder, data);
                } else {
                    onClickSubButton(viewHolder, data);
                }
                Log.e("listener", String.valueOf(position));
            }
        });

        return convertView;
    }

    public void onClickAddButton(final GameListHolder viewHolder, final GameListData data) {
        //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
        //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언

        final View dialogView = layoutInflater.inflate(R.layout.dialog_set_nickname, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setIcon(data.getAppIcon());
        builder.setTitle(data.getAppName());
        builder.setView(dialogView);

        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.setFlag(true);
                viewHolder.button_regist.setText("SUB");
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
        //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
        //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언

        final View dialogView = layoutInflater.inflate(R.layout.dialog_sub_application, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setIcon(data.getAppIcon());
        builder.setTitle(data.getAppName());
        builder.setView(dialogView);

        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.setFlag(false);
                viewHolder.button_regist.setText("ADD");
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
