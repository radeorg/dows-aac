package org.dows.aac.yml;

public interface AacConfig {
    LoginSetting getLoginSetting();

    String[] getWhitelist();

    JwtSetting getJwtSetting();

    boolean isEnableLogin();
}
