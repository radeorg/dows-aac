package org.dows.aac.api;

import org.dows.framework.api.uim.AccountInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.HashMap;
import java.util.Map;

public interface AacApi {

//    void setCache(String cacheName, Object key, Object value);
//
//    Object getCache(String cacheName, Object key);
//
//    void removeCache(String cacheName, Object key);
//
//    void clearCache(String cacheName);
//
//    /**
//     * 当rbac域的角色发送变化时，同步账号权限
//     */
//    void syncAccountPermission(SyncAccountPermissionRequest syncAccountPermissionRequest);

    AacUser getCurrentAccUser();

//    AacUser getAccUser();
}
