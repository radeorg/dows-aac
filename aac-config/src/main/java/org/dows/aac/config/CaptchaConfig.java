//package org.dows.aac.config;
//
//import cn.hutool.captcha.generator.CodeGenerator;
//import cn.hutool.captcha.generator.MathGenerator;
//import cn.hutool.captcha.generator.RandomGenerator;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.awt.*;
//
///**
// * 验证码自动装配配置
// */
//@RequiredArgsConstructor
//@Configuration
////@EnableConfigurationProperties(CaptchaProperties.class)
//public class CaptchaConfig {
//    private final CaptchaProperties captchaProperties;
//
//    /**
//     * 验证码文字生成器
//     *
//     * @return CodeGenerator
//     */
//    @Bean
//    public CodeGenerator codeGenerator() {
//        String codeType = captchaProperties.getCode().getType();
//        int codeLength = captchaProperties.getCode().getLength();
//        if ("math".equalsIgnoreCase(codeType)) {
//            return new MathGenerator(codeLength);
//        } else if ("random".equalsIgnoreCase(codeType)) {
//            return new RandomGenerator(codeLength);
//        } else {
//            throw new IllegalArgumentException("Invalid captcha generator type: " + codeType);
//        }
//    }
//
//    /**
//     * 验证码字体
//     */
//    @Bean
//    public Font captchaFont() {
//        String fontName = captchaProperties.getFont().getName();
//        int fontSize = captchaProperties.getFont().getSize();
//        int fontWight = captchaProperties.getFont().getWeight();
//        return new Font(fontName, fontWight, fontSize);
//    }
//
//
//}
