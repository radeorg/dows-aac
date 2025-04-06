package org.dows.aac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author
 * @description
 * @date 2024年2月26日 下午12:06:05
 */
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"org.dows.rade", "org.dows.aac"})
public class AacApplication {
    public static void main(String[] args) {
        SpringApplication.run(AacApplication.class, args);
    }


}

