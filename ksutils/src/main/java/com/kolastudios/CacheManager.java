package com.kolastudios;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CacheManager {
    private static Cache getCache(String url){
        return Cache.findByField(Cache.class, "MD5", KSUtils.md5(url));
    }

    private static void saveCache(Cache cache){
        Cache existing = getCache(cache.url);

        if(existing != null){
            existing.content = cache.content;
            existing.save();
        }else{
            cache.save();
        }
    }

    static class CacheRequest<T>{
        CacheRequest(Call<T> call, boolean skipCache, boolean abortIfCached, final KSCallback<T> callback){
            execute(call, skipCache, abortIfCached, callback);
        }

        private void execute(Call<T> call, boolean skipCache, boolean abortIfCached, final KSCallback<T> callback){
            boolean cached = false;

            if(!skipCache){
                Cache cache = CacheManager.getCache(call.request().url().toString());
                if(cache != null && !TextUtils.isEmpty(cache.content)){
                    try{
                        T body = new Gson().fromJson(cache.content, new TypeToken<T>(){}.getType());
                        KSResponse<T> response = new KSResponse<>(body, 200, true, true);
                        callback.callback(response);
                        cached = true;
                    }catch (Exception ex){
                        KSUtils.logE("Error converting cache to object -> " + ex.getMessage());
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
    public static <T> void get(Call<T> call, boolean skipCache, boolean abortIfCached, final KSCallback<T> callback){
        new CacheRequest<T>(call, skipCache, abortIfCached, callback);
    }

    /**
     * Make a GET request that returns cached results if available and network results
     * @param call The original Retrofit call for the request
     * @param callback KSCallback to receive responses from the cache or network calls
     * @param <T> The response type
     */
    public static <T> void get(Call<T> call, final KSCallback<T> callback){
        new CacheRequest<T>(call, false, false, callback);
    }
}
