package com.wraptalk.wraptalk.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jiyoungpark on 15. 11. 10..
 */
public class DBManager extends SQLiteOpenHelper {

    static DBManager g_this;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        g_this = this;
    }

    public static DBManager getInstance() {
        return g_this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String sql = "CREATE TABLE IF NOT EXISTS user_info(" +
                        "token TEXT PRIMARY KEY, " +
                        "device_id TEXT, " +
                        "user_id TEXT, " +
                        "gcm_key TEXT);";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS app_info(" +
                "package_name TEXT PRIMARY KEY UNIQUE, " +
                "app_name TEXT, " +
                "nickname TEXT, " +
                "check_registration INTEGER);"; // boolean처럼 사용할 것. 앱 등록 여부.
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS chat_info(" +
                "channel_id TEXT PRIMARY KEY UNIQUE, " +
                "package_name TEXT, " +
                "channel_title TEXT, " +
                "master_id TEXT, " +
                "user_color TEXT);";
        db.execSQL(sql);

        Log.e("DBManager", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void write(String _query) {
        Log.e("DBManager", _query);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void select(String query, OnSelect cb) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            cb.onSelect(cursor);
        }
        cb.onComplete();
    }

    public static interface OnSelect {
        public void onSelect(Cursor cursor);
        public void onComplete();
    }
}