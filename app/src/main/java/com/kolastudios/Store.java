package com.kolastudios;

import com.google.gson.annotations.SerializedName;

public class Store extends KSModel {
    public String name;
    public String address;

    @SerializedName("delivery_time")
    public String deliveryTime;

    @SerializedName("opening_hours")
    public String openingHours;

    public double lat;
    public double lng;

    @SerializedName("image_url")
    public String imageUrl;

    public String cuisines;

    public Store(){}
}
