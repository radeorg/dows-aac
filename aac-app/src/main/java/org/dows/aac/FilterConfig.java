//package org.dows.aac;
//
//import org.dows.rade.web.filter.AppContextCleanupFilter;
//import org.dows.rade.web.filter.AppContextSetupFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<AppContextSetupFilter> appIdFilter() {
//        FilterRegistrationBean<AppContextSetupFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new AppContextSetupFilter());
//        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);  // 高优先级先执行
//        return registration;
//    }
//
//    @Bean
//    public FilterRegistrationBean<AppContextCleanupFilter> cleanupFilter() {
//        FilterRegistrationBean<AppContextCleanupFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new AppContextCleanupFilter());
//        registration.setOrder(Ordered.LOWEST_PRECEDENCE);  // 低优先级最后执行
//        return registration;
//    }
//}