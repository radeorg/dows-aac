package org.dows.aac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author
 * @description
 * @date 2024年2月26日 下午12:06:05
 */
@SpringBootApplication(scanBasePackages = {"org.dows.framework", "org.dows.aac", "com.shdy.admin"})
public class AacApplication {
    public static void main(String[] args) {
        SpringApplication.run(AacApplication.class, args);
    }
}

