package org.dows.aac.api.constant;

import lombok.Getter;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 2/28/2024 9:55 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public enum AuthKey {
    CACHE_KEY_PREFIX("AAC:ID:");


    @Getter
    private String keyPrefix;

    AuthKey(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String buildKey(String key) {
        if (key.isEmpty()) {
            throw new RuntimeException("key不能为空");
        }
        return keyPrefix + key;
    }

}
