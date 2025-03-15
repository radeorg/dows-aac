package org.dows.aac;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import jakarta.annotation.PostConstruct;
import org.apache.ibatis.annotations.Mapper;
import org.dows.rbac.handler.RbacDataPermissionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"org.dows.aac", "org.dows.uat.account.mapper", "org.dows.uat.user.mapper", "org.dows.rbac.mapper","org.dows.app.mapper"}, annotationClass = Mapper.class)
public class MybatisConfig {
//    @Autowired
//    @Lazy
//    private RbacDataPermissionHandler rbacDataPermissionHandler;

    /**
     * 分页插件
     */
    @PostConstruct
    public void init() {
        System.out.println("MybatisPlusInterceptor init");
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        interceptor.addInnerInterceptor(new DataPermissionInterceptor(rbacDataPermissionHandler));
        return interceptor;
    }

}