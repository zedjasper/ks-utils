package com.kolastudios;

import android.os.Bundle;

import com.aquery.AQuery;

import java.util.List;

import retrofit2.Call;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Call<List<Store>> call = apiInterface.getStores(1);

        CacheManager.get(call, false, false, new KSCallback<List<Store>>() {
            @Override
            public void callback(KSResponse<List<Store>> response) {
                KSUtils.log("callback() -> " + response.code() + " : " + response.isCache());
                if(response.isSuccessful()){
                    if(!response.isCache()){
                        for(Store store: response.body()){
                            store.save();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable t, boolean isCache) {
                KSUtils.logE("onError() -> " + t.getMessage() + " : " + isCache);
            }
        });

        AQuery aq = new AQuery(this);
        aq.id(R.id.img_test).image("https://mdundo.com/media/articles/1440675435_2706_b.jpg");
    }
}