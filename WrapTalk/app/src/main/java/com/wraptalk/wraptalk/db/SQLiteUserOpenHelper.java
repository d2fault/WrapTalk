package com.wraptalk.wraptalk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jiyoungpark on 15. 11. 10..
 */
public class SQLiteUserOpenHelper extends SQLiteOpenHelper {

    public SQLiteUserOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS user_info(" +
                        "token TEXT PRIMARY KEY , " +
                        "device_id TEXT, " +
                        "user_id TEXT, " +
                        "gcm_key TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
