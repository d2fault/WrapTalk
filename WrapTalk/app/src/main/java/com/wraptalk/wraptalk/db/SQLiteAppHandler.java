package com.wraptalk.wraptalk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jiyoungpark on 15. 11. 10..
 */
public class SQLiteAppHandler {

    static String TABLE_NAME = "app_info";
    SQLiteAppOpenHelper helper;
    SQLiteDatabase db;

    // 초기화 작업
    private SQLiteAppHandler(Context context) {
        helper = new SQLiteAppOpenHelper(context, "appinfo.sqlite", null, 1);
    }

    //open
    public static SQLiteAppHandler open(Context context) {
        return new SQLiteAppHandler(context);
    }

    //close
    public void close() {
        db.close();
    }

    //저장
    public void insert(String package_name, String app_name, String nickname, Integer check_registration) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("package_name", package_name);
        values.put("app_name", app_name);
        values.put("nickname", nickname);
        values.put("check_registration", check_registration);
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
