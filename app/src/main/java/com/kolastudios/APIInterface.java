package com.kolastudios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zed on 3/22/2018.
 */

public interface APIInterface {
    @GET("get_cloud_kitchens")
    Call<List<Store>> getStores(@Query("area_id") long areaId);
}
