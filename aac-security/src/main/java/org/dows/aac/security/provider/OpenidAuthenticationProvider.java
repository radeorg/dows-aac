package org.dows.aac.security.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.request.LoginRequest;
import org.dows.aac.security.UserDetailsServiceHandler;
import org.dows.aac.security.token.OpenidAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
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

        LoginRequest loginRequest =  (LoginRequest) authentication.getDetails();
        //查询账号标识
        UserDetails userDetails = userDetailsServiceHandler.loadUserByOpenId(loginRequest);
        if(userDetails == null){
            //LoginRequest loginRequest =  (LoginRequest) authentication.getDetails();
            userDetailsServiceHandler.newRegister(authentication.getName(), "",loginRequest);
            // todo 从新查询一次，走一遍流程
            userDetails = userDetailsServiceHandler.loadUserByOpenId(loginRequest);
            //return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
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
