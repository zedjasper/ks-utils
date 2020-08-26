A set of common utilities for an Android project. It includes SugarORM, Prefs, Retrofit, Google Gson and other utility methods.

You do not need to add retrofit, gson, sugarorm and prefs to your application. This library will add those for you.

### How to setup

Use a custom application class and add this code in the onCreate() method
```java
new KSUtils.Builder()
                .setContext(this)
                .setLogTag(Constants.LOG_TAG)
                .setLogEnabled(Constants.LOG_ENABLED)
                .build();
```

Add these to your AndroidManifest.xml

```xml
<meta-data
    android:name="DATABASE"
    android:value="dbname.db" />
<meta-data
    android:name="VERSION"
    android:value="1" />
<meta-data
    android:name="DOMAIN_PACKAGE_NAME"
    android:value="package_name" />
<meta-data
    android:name="QUERY_LOG"
    android:value="true" />
```

`dbname.db` is the name of your database. `package_name` is the name of your android application package.

Below is an example of how to get a response from the server and return a cached copy if available
```java
Call<List<Store>> call = apiInterface.getStores(1);

CacheManager.get(call, new KSCallback<List<Store>>() {
    @Override
    public void callback(KSResponse<List<Store>> response) {
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
```