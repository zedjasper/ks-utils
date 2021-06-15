package com.kolastudios;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CacheManager {
    static final String TABLE_NAME = "cache";
    static final String COLUMN_ID = "id";
    static final String COLUMN_MD5 = "md5";
    static final String COLUMN_URL = "url";
    static final String COLUMN_CONTENT = "content";

    private static Cache getCache(String url){
        String md5 = KSUtils.md5(url);

        SQLiteDatabase db = KSUtils.dbHandler.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, COLUMN_MD5);
        Cursor cursor = db.rawQuery(query, new String[]{md5});

        Cache cache = null;

        try {
            if (cursor.moveToFirst()) {
                cache = new Cache();
                cache.md5 = md5;
                cache.url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
                cache.content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
            }
        } catch (Exception e) {
            KSUtils.logE("Error while trying to get cache from database -> " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return cache;
    }

    private static void saveCache(Cache cache){
        if(cache.md5 == null){
            cache.md5 = KSUtils.md5(cache.url);
        }

        SQLiteDatabase db = KSUtils.dbHandler.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MD5, cache.md5);
            values.put(COLUMN_URL, cache.url);
            values.put(COLUMN_CONTENT, cache.content);

            int rows = db.update(TABLE_NAME, values, COLUMN_MD5 + "= ?", new String[]{cache.md5});

            if (rows == 0) {
                db.insertOrThrow(TABLE_NAME, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            KSUtils.logE("Error while trying to add or update cache -> " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    static class CacheRequest<T>{
        CacheRequest(Call<T> call, boolean skipCache, boolean abortIfCached, TypeToken type, final KSCallback<T> callback){
            execute(call, skipCache, abortIfCached, type, callback);
        }

        private void execute(Call<T> call, boolean skipCache, boolean abortIfCached, TypeToken type, final KSCallback<T> callback){
            boolean cached = false;

            if(!skipCache){
                Cache cache = CacheManager.getCache(call.request().url().toString());
                if(cache != null && !TextUtils.isEmpty(cache.content)){
                    try{
                        T body = new Gson().fromJson(cache.content, type.getType());
                        KSResponse<T> response = new KSResponse<>(body, 200, true, true);
                        callback.callback(response);
                        cached = true;
                    }catch (Exception ex){
                        callback.onError(ex, true);
                    }
                }
            }

            if(abortIfCached && cached){
                return;
            }

            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    callback.callback(new KSResponse<T>(response.body(), response.code(), false, response.isSuccessful()));

                    if(response.isSuccessful() && response.body() != null){
                        Cache cache = new Cache(call.request().url().toString(), new Gson().toJson(response.body()));
                        CacheManager.saveCache(cache);
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    callback.onError(t, false);
                }
            });
        }
    }

    /**
     * Make a GET request that returns either the cached results or the network results or both
     * @param call The original Retrofit call for the request
     * @param skipCache Whether or not to return cached results if available
     * @param abortIfCached Whether or not to ignore network results if cache available
     * @param callback KSCallback to receive responses from the cache or network calls
     * @param <T> The response type
     */
    public static <T> void get(Call<T> call, boolean skipCache, boolean abortIfCached, TypeToken type, final KSCallback<T> callback){
        new CacheRequest<T>(call, skipCache, abortIfCached, type, callback);
    }

    /**
     * Make a GET request that returns cached results if available and network results
     * @param call The original Retrofit call for the request
     * @param callback KSCallback to receive responses from the cache or network calls
     * @param <T> The response type
     */
    public static <T> void get(Call<T> call, TypeToken type, final KSCallback<T> callback){
        new CacheRequest<T>(call, false, false, type, callback);
    }
}