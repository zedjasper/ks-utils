package com.kolastudios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHandler extends SQLiteOpenHelper {

    static final String DB_NAME = "kscache.db";
    static final int DB_VERSION = 1;

    private static DBHandler sInstance;

    public static synchronized DBHandler getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHandler(context.getApplicationContext());
        }

        return sInstance;
    }

    private DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CACHE_TABLE = "CREATE TABLE " + CacheManager.TABLE_NAME +
                "(" +
                CacheManager.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CacheManager.COLUMN_MD5 + " TEXT, " +
                CacheManager.COLUMN_URL + " TEXT, " +
                CacheManager.COLUMN_CONTENT + " TEXT" +
                ")";

        KSUtils.log(CREATE_CACHE_TABLE);

        db.execSQL(CREATE_CACHE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CacheManager.TABLE_NAME);
            onCreate(db);
        }
    }
}