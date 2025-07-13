package org.dows.aac.config;

import lombok.RequiredArgsConstructor;
import org.dows.aac.AacSettings;
import org.dows.rade.web.filter.AppContextCleanupFilter;
import org.dows.rade.web.filter.AppContextSetupFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {


    private final AacSettings aacSettings;

    //    private final
    @Bean
    public FilterRegistrationBean<AppContextSetupFilter> appIdFilter() {
        FilterRegistrationBean<AppContextSetupFilter> registration = new FilterRegistrationBean<>();
        AppContextSetupFilter appContextSetupFilter = new AppContextSetupFilter();
        //String[] whitelist = aacSettings.getWhitelist();
        //appContextSetupFilter.setWhitelist(whitelist);

        // todo 需要增加tenantApp 映射表
        /*List<String> appIds =  tenantAppApi.listAppId();
        for (String appId : appIds) {
            appContextSetupFilter.syncWhitelistByAppId(appId);
        }*/
        registration.setFilter(appContextSetupFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);  // 高优先级先执行
        return registration;
    }

    @Bean
    public FilterRegistrationBean<AppContextCleanupFilter> cleanupFilter() {
        FilterRegistrationBean<AppContextCleanupFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AppContextCleanupFilter());
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);  // 低优先级最后执行
        return registration;
    }
}