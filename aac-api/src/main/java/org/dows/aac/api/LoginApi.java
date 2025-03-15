package org.dows.aac.api;

import jakarta.servlet.http.HttpServletRequest;
import org.dows.aac.api.request.LoginRequest;
import org.dows.aac.api.response.LoginResponse;
import org.dows.framework.api.Response;

public interface LoginApi {
    /**
     * 登入
     *
     * @param loginRequest
     * @param request
     * @return
     */
    Response<LoginResponse> login(LoginRequest loginRequest, HttpServletRequest request);

    /**
     * 登出
     *
     * @param id
     */
    void logout(String id);
}
