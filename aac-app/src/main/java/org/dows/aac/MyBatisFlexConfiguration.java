package org.dows.aac;

import com.mybatisflex.core.audit.AuditManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MyBatisFlexConfiguration {

    @Value("${rade.log.sql.printSql:false}")
    private boolean printSql;

    public MyBatisFlexConfiguration() {
        if (printSql) {
            //开启审计功能
            AuditManager.setAuditEnable(true);
            //设置 SQL 审计收集器
            AuditManager.setMessageCollector(auditMessage ->
                    log.info("{},{}ms", auditMessage.getFullSql(), auditMessage.getElapsedTime())
            );
        }
    }
}