package com.wraptalk.wraptalk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jiyoungpark on 15. 11. 10..
 */
public class SQLiteUserHandler {

    static String TABLE_NAME = "user_info";
    SQLiteUserOpenHelper helper;
    SQLiteDatabase db;

    // 초기화 작업
    private SQLiteUserHandler(Context context) {
        helper = new SQLiteUserOpenHelper(context, "userinfo.sqlite", null, 1);
    }

    //open
    public static SQLiteUserHandler open(Context context) {
        return new SQLiteUserHandler(context);
    }

    //close
    public void close() {
        db.close();
    }

    //저장
    public void insert(String token, String device_id, String user_id,  String gcm_key) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("token", token);
        values.put("device_id", device_id);
        values.put("user_id", user_id);
        values.put("gcm_key", gcm_key);
        db.insert(TABLE_NAME, null, values);
    }

    //수정
    public void update(String token, String column, String data) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column, data);
        db.update(TABLE_NAME, values, "token=?", new String[]{token});
    }

    //삭제
    public void delete() {
        db = helper.getWritableDatabase();
        db.delete(TABLE_NAME, "token=?", new String[]{"token"});
    }

    //검색
    public Cursor select() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }
}
