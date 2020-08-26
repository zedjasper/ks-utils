package com.kolastudios;
/**
 * Holds a cache object
 * url: The url of the cached results
 * md5: The md5 hash for the url
 * content: The actual content of the cache
 */
public class Cache extends KSModel {
    public String url;
    public String md5;
    public String content;

    public Cache(){}

    public Cache(String url, String content){
        this.url = url;
        this.content = content;
    }

    @Override
    public long save() {
        this.md5 = KSUtils.md5(this.url);

        return super.save();
    }
}
