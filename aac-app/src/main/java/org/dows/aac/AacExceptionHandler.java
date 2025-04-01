package org.dows.aac;

import org.dows.rade.exception.RadeExceptionHandler;
import org.dows.rade.web.UnifiedMessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/***
 * 统一封装异常、统一处理出参
 */
@RestControllerAdvice
public class AacExceptionHandler extends RadeExceptionHandler {

    public AacExceptionHandler(UnifiedMessageSource unifiedMessageSource) {
        super(unifiedMessageSource);
    }
}
