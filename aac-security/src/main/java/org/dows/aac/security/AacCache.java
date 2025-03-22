package org.dows.aac.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.CacheAPI;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AacCache {

    private final CacheAPI cacheAPI;

    public Object getCacheValue(String cacheName, Object key) {
        return cacheAPI.getCacheValue(cacheName, key);
    }

    public void putCache(String cacheName, Object key, Object value) {
        cacheAPI.putCache(cacheName, key, value);
    }

    public void evictCache(String cacheName, Object key) {
        cacheAPI.evictCache(cacheName, key);
    }

    public void clearCaches(String cacheName) {
        cacheAPI.clearCaches(cacheName);
    }

}
