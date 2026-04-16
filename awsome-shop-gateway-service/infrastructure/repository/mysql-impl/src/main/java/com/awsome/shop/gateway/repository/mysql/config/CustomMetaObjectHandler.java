package com.awsome.shop.gateway.repository.mysql.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 元数据自动填充处理器
 *
 * <p>自动填充审计字段：createdAt, updatedAt, deleted, version</p>
 *
 * <p>注意：Gateway 使用 WebFlux 响应式环境，不支持 ThreadLocal。
 * createdBy/updatedBy 字段不在 Gateway 层填充。</p>
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();

        // 时间字段
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);

        // 逻辑删除和版本号
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "version", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();

        // 时间字段
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, now);
    }
}
