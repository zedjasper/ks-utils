package com.kolastudios;

import androidx.annotation.Nullable;

/**
 * Network or cached response object
 * @param <T> The response body type
 */
public class KSResponse<T> {
    private T body;
    private int code;
    private boolean isCache;
    private boolean isSuccessful;

    public KSResponse(@Nullable T body, int code, boolean isCache, boolean isSuccessful){
        this.body = body;
        this.code = code;
        this.isCache = isCache;
        this.isSuccessful = isSuccessful;
    }

    /**
     * Whether the response is from cache or network
     * @return boolean
     */
    public boolean isCache(){
        return isCache;
    }

    /**
     * The HTTP response code
     * @return integer
     */
    public int code(){
        return code;
    }

    /**
     * The response body. Please note that this can be null
     * @return T
     */
    public T body(){
        return body;
    }

    /**
     * Whether or not the request was successful
     * @return boolean
     */
    public boolean isSuccessful(){
        return isSuccessful;
    }
}
