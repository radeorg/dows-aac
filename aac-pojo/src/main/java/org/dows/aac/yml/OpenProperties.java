package org.dows.aac.yml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dows.open")
@Data
public class OpenProperties {

    private String appId;
    private String thirdAppId;
    private String channel;
    private String secret;
    private String token;

}
