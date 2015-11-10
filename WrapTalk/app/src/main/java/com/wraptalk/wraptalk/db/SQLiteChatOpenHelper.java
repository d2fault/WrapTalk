package com.wraptalk.wraptalk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jiyoungpark on 15. 11. 10..
 */
public class SQLiteChatOpenHelper extends SQLiteOpenHelper {
    public SQLiteChatOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS chat_info(" +
                        "package_name TEXT primary key, " +
                        "channel_title TEXT, " +
                        "channel_password INTEGER, " +
                        "master_id TEXT, " +
                        "check_onoff INTEGER, " +
                        "fixed_number INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
