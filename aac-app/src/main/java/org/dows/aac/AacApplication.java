package org.dows.aac;

import lombok.RequiredArgsConstructor;
import org.dows.rade.web.ResponseWrapperHandler;
import org.dows.rade.web.UnifiedMessageSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

/**
 * @author
 * @description
 * @date 2024年2月26日 下午12:06:05
 */
@SpringBootApplication(scanBasePackages = {"org.dows.rade", "org.dows.aac"})
public class AacApplication {
    public static void main(String[] args) {
        SpringApplication.run(AacApplication.class, args);
    }


}

