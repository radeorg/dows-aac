package org.dows.aac.security.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.request.LoginRequest;
import org.dows.aac.security.UserDetailsServiceHandler;
import org.dows.aac.security.token.OpenidAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
//@Component
@RequiredArgsConstructor
public class OpenidAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceHandler userDetailsServiceHandler;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OpenidAuthenticationToken authenticationToken = (OpenidAuthenticationToken) authentication;
        //根据手机号（Principal）去查用户信息
        UserDetails userDetails = userDetailsServiceHandler.loadUserByUsername((String) authentication.getPrincipal());
        if (userDetails == null) {
            userDetailsServiceHandler.newRegister(authentication.getName(), "", (LoginRequest) authentication.getDetails());
            // todo 从新查询一次，此时查询不到
            userDetails = userDetailsServiceHandler.loadUserByUsername(authentication.getName());
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        /*if (userDetails == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }*/
        //将认证信息传入进去。
        OpenidAuthenticationToken authenticationResult = new OpenidAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        //将请求的信息传递Token中。
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(OpenidAuthenticationToken.class);
    }
}
