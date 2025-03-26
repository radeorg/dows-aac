package org.dows.aac.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.AacSettings;
import org.dows.aac.api.AacApi;
import org.dows.aac.api.AacUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AacHandler implements AacApi {
    private final AacSettings aacSettings;

    @Override
    public AacUser getCurrentAccUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AacUser) {
            return (AacUser) principal;
        }
        if (!aacSettings.isEnableLogin()) {
            SecurityContext securityContext = new SecurityContextImpl();
            Map<String, Object> credits = new HashMap<>();
            credits.put("roleId", 1L);
            credits.put("role", "admin");
            Long accountId = 1L;
            String userName = "shdy";
            String password = "123456";
            Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
            List<Long> roleIds = List.of(1L);
            AacUser aacUser = new DefaultAacUser(accountId, userName, password, authorities, roleIds, false);
            /*Authentication authentication = new UsernamePasswordAuthenticationToken(aacUser, credits);
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);*/
            return aacUser;
        }

        return (AacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    //private final CaffeineTemplate caffeineTemplate;




    /*@Override
    public void setCache(String cacheName, Object key, Object value) {
        caffeineTemplate.putCache(cacheName, key, value);
    }

    @Override
    public Object getCache(String cacheName, Object key) {
        return caffeineTemplate.getCacheValue(cacheName, key);
    }

    @Override
    public void removeCache(String cacheName, Object key) {
        caffeineTemplate.evictCache(cacheName,key);
    }

    @Override
    public void clearCache(String cacheName) {
        caffeineTemplate.clearCaches(cacheName);
    }*/
}
