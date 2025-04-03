package org.dows.aac.api;


import org.dows.aac.api.constant.OpenApiEnum;
import org.dows.aac.api.constant.OpenChannel;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiMapping {
    OpenChannel channel();

    OpenApiEnum func();
}
