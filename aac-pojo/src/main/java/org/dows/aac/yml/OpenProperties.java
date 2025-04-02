package org.dows.aac.yml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

//@Component
@ConfigurationProperties(prefix = "dows.channel")
@Data
public class OpenProperties {

    private List<OpenSetting> opens;

}
