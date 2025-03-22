package org.dows.aac.api;

public interface CacheAPI {
    Object getCacheValue(String cacheName, Object key);

    void putCache(String cacheName, Object key, Object value);

    void evictCache(String cacheName, Object key);

    void clearCaches(String cacheName);
}
