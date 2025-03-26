package org.dows.aac.api;

public interface Cacheable {

    default Object getCacheValue(String cacheName, Object key) {
        return null;
    }

    default void putCache(String cacheName, Object key, Object value) {
    }

    default void evictCache(String cacheName, Object key) {


    }

    default void clearCaches(String cacheName) {

    }
}
