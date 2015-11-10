package com.wraptalk.wraptalk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jiyoungpark on 15. 11. 10..
 */
public class SQLiteAppOpenHelper extends SQLiteOpenHelper {

    public SQLiteAppOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS app_info(" +
                        "package_name TEXT primary key, " +
                        "app_name TEXT, " +
                        "nickname TEXT, " +
                        "check_registration INTEGER);"; // boolean처럼 사용할 것. 앱 등록 여부.
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
