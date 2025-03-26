package org.dows.aac.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PhoneCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object phone;//手机号

    private Object code;//验证码


    public PhoneCodeAuthenticationToken(Object phone, Object code) {
        super(null);
        this.phone = phone;
        this.code = code;
        setAuthenticated(false);
    }

    public PhoneCodeAuthenticationToken(Object phone, Object code, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = phone;
        this.code = code;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}
