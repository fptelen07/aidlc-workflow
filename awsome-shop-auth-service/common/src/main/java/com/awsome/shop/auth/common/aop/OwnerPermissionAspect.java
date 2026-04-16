package com.awsome.shop.auth.common.aop;

import com.awsome.shop.auth.common.annotation.RequireOwnerPermission;
import com.awsome.shop.auth.common.enums.ErrorCode;
import com.awsome.shop.auth.common.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 资源所有者权限校验 AOP 切面
 *
 * <p>拦截标注 @RequireOwnerPermission 的方法，校验当前用户是否为资源所有者。</p>
 * <p>需要子类实现 getCurrentUserId() 和 isAdmin() 方法。</p>
 */
@Aspect
@Component
public class OwnerPermissionAspect {

    private static final String AUTHZ_CODE = "AUTHZ_001";
    private static final String AUTHZ_MSG = "无权操作此资源";

    @Around("@annotation(requireOwnerPermission)")
    public Object checkOwnership(ProceedingJoinPoint joinPoint, RequireOwnerPermission requireOwnerPermission) throws Throwable {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(AUTHZ_CODE, AUTHZ_MSG);
        }

        // 管理员跳过检查
        if (requireOwnerPermission.allowAdmin() && isAdmin()) {
            return joinPoint.proceed();
        }

        // 从方法参数中提取资源所有者 ID
        String paramName = requireOwnerPermission.resourceIdParam();
        Long resourceOwnerId = extractParamValue(joinPoint, paramName);

        if (resourceOwnerId != null && !currentUserId.equals(resourceOwnerId)) {
            throw new BusinessException(AUTHZ_CODE, AUTHZ_MSG);
        }

        return joinPoint.proceed();
    }

    /**
     * 获取当前用户 ID — 通过反射调用 UserContext.getCurrentUserId()
     * 避免 common 模块直接依赖 mysql-impl 模块
     */
    protected Long getCurrentUserId() {
        try {
            Class<?> userContextClass = Class.forName(getUserContextClassName());
            Method method = userContextClass.getMethod("getCurrentUserId");
            return (Long) method.invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断当前用户是否为管理员 — 子类可覆盖
     */
    protected boolean isAdmin() {
        // 默认返回 false，后续 Auth 服务实现角色判断后可扩展
        return false;
    }

    /**
     * 获取 UserContext 类全限定名 — 子类可覆盖
     */
    protected String getUserContextClassName() {
        // 默认路径，各服务包名不同需要子类覆盖
        return "unknown";
    }

    private Long extractParamValue(ProceedingJoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(paramName) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }
        return null;
    }
}
