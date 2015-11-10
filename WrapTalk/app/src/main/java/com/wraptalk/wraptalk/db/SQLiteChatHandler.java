package com.wraptalk.wraptalk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jiyoungpark on 15. 11. 10..
 */
public class SQLiteChatHandler {

    static String TABLE_NAME = "chat_info";
    SQLiteChatOpenHelper helper;
    SQLiteDatabase db;

    // 초기화 작업
    private SQLiteChatHandler(Context context) {
        helper = new SQLiteChatOpenHelper(context, "chatinfo.sqlite", null, 1);
    }

    //open
    public static SQLiteChatHandler open(Context context) {
        return new SQLiteChatHandler(context);
    }

    //close
    public void close() {
        db.close();
    }

    //저장
    public void insert(String package_name, String channel_title, Integer channel_password, String master_id, Integer check_onoff, Integer fixed_number) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("package_name", package_name);
        values.put("channel_title", channel_title);
        values.put("channel_password", channel_password);
        values.put("master_id", master_id);
        values.put("check_onoff", check_onoff);
        values.put("fixed_number", fixed_number);
        db.insert(TABLE_NAME, null, values);
    }

    //수정
    public void update(String token, String column, String data) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column, data);
        db.update(TABLE_NAME, values, "package_name=?", new String[]{token});
    }

    //삭제
    public void delete() {
        db = helper.getWritableDatabase();
        db.delete(TABLE_NAME, "package_name=?", new String[]{"package_name"});
    }

    //검색
    public Cursor select() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("package_name", null, null, null, null, null, null);
        return cursor;
    }
}
