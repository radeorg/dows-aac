package org.dows.aac.api;

public interface AacVerifyConfig {

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
