package org.dows.aac.config;

import lombok.RequiredArgsConstructor;
import org.dows.aac.api.AacVerifyConfig;
import org.dows.aac.yml.AacProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 3/13/2024 3:26 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({AacProperties.class})
//@ConditionalOnProperty(name = "aac.enabled", havingValue = "true", matchIfMissing = true)
public class AacConfig implements AacVerifyConfig {
    private final AacProperties aacProperties;



    /**
     * 使用启动登录
     *
     * @return
     */
    public boolean isEnableLogin() {
        return aacProperties.getLoginSetting().isEnabled();
    }

    /**
     * 获取白名单
     *
     * @return
     */
    public String[] getWhitelist() {
        return aacProperties.getWhitelist();
    }


    public void setEnableLogin(Boolean enable) {
        aacProperties.getLoginSetting().setEnabled(enable);
    }
}


