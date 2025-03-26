package org.dows.aac;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestCache implements Cacheable {


    public Object getCacheValue(String cacheName, Object key) {
        return null;
    }

    public void putCache(String cacheName, Object key, Object value) {
    }

    public void evictCache(String cacheName, Object key) {


    }

    public void clearCaches(String cacheName) {

    }

}
