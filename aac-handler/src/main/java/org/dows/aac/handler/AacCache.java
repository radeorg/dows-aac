package org.dows.aac.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.framework.cache.caffeine.CaffeineTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AacCache {
    private final CaffeineTemplate caffeineTemplate;


    public Object getCacheValue(String cacheName, Object key) {
        return caffeineTemplate.getCacheValue(cacheName, key);
    }

    public void putCache(String cacheName, Object key, Object value) {
        caffeineTemplate.putCache(cacheName, key, value);
    }

    public void evictCache(String cacheName, Object key) {
        caffeineTemplate.evictCache(cacheName, key);
    }

    public void clearCaches(String cacheName) {
        caffeineTemplate.clearCaches(cacheName);
    }

}
