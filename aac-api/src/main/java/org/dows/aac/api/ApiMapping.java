package org.dows.aac.api;


import org.dows.aac.constant.OpenApiEnum;
import org.dows.rade.constant.OpenChannel;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiMapping {
    OpenChannel channel();

    OpenApiEnum func();
}
