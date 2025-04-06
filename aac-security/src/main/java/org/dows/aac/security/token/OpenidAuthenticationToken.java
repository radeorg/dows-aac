package org.dows.aac.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OpenidAuthenticationToken extends AbstractAuthenticationToken {

    private final Object openid;

    private Object code;

    public OpenidAuthenticationToken(Object openid, Object code) {
        super(null);
        this.openid = openid;
        this.code = code;
        setAuthenticated(false);
    }

    public OpenidAuthenticationToken(Object openid, Object code, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.openid = openid;
        this.code = code;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return openid;
    }
}
