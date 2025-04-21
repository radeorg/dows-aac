package org.dows.aac.api;

import jakarta.servlet.http.HttpServletRequest;
import org.dows.aac.request.LoginRequest;
import org.dows.aac.response.LoginResponse;

public interface LoginApi {
    /**
     * 登入
     *
     * @param loginRequest
     * @param request
     * @return
     */
    LoginResponse login(LoginRequest loginRequest, HttpServletRequest request);

    /**
     * 登出
     *
     * @param id
     */
    void logout(String id);

//    LoginResponse openIdLogin(String openid, IdentifierType identifierType);
}
