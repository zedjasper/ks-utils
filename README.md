A set of common utilities for an Android project. It includes SugarORM, Prefs, Retrofit, Google Gson, AQuery and other utility methods.

You do not need to add retrofit, gson, sugarorm, aquery and prefs to your application. This library will add those for you.

### How to setup

Add it in your root build.gradle at the end of repositories

`allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}`

Add the dependency

`dependencies {
        implementation 'com.github.zedjasper:ks-utils:1.0'
}`

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

### Utility Methods
```java
KSUtils.log(String message); //Logs a message using the LOG_TAG in config
KSUtils.logE(String message); //Logs an error message
KSUtils.md5(String s); //Returns md5 hash of a string
KSUtils.getInitials(String s); //Returns initials of a string e.g Kola Studios = KS
KSUtils.isValidEmail(String email); //Returns whether an email address is valid or not
KSUtils.printFacebookKeyHash(String package);
KSUtils.getVersionCode(); //Returns the android version code
KSUtils.getVersionName(); //Returns the android version name
```