package org.dows.aac;

import org.dows.aac.yml.JwtSetting;
import org.dows.aac.yml.LoginSetting;

public interface AacSettings {
    LoginSetting getLoginSetting();


    JwtSetting getJwtSetting();
    /**
     * 是否需要登录
     *
     * @return
     */
    boolean isEnableLogin();

    /**
     * 设置是否需要登录
     *
     * @param enable
     */
    void setEnableLogin(Boolean enable);

    /**
     * 获取白名单
     *
     * @return
     */
    String[] getWhitelist();




}
