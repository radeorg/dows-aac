package org.dows.aac.yml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 3/13/2024 3:23 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
@ConfigurationProperties(prefix = "dows.aac")
public class AacProperties {

    private LoginSetting loginSetting;
    private String[] whitelist;
    private JwtSetting jwtSetting;

}

