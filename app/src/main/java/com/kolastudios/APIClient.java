package com.kolastudios;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Zed on 3/22/2018.
 */

public class APIClient {
    public static Retrofit getClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        gsonBuilder.registerTypeAdapter(Store.class, (JsonDeserializer<Store>) (json, typeOfT, context) -> {
            JsonObject j = json.getAsJsonObject();
            Gson g = new Gson();
            Store d = g.fromJson(json, Store.class);
            d.setId(j.get("id").getAsLong());
            return d;
        });

        return new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(client)
                .build();
    }
}
