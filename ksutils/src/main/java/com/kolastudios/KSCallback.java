package com.kolastudios;

/**
 * Callback for cached and network results
 * @param <T> The response type
 */
public interface KSCallback<T> {
    /**
     * Callback for cache or network results.
     * @param response The KSResponse object with body(), code() and whether it's cache results or not
     */
    void callback(KSResponse<T> response);

    /**
     * An error that occurs while getting cache or network results
     * @param t The exception
     * @param isCache Whether it was during cache creation or network call
     */
    void onError(Throwable t, boolean isCache);
}
