package io.train.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 自定义系统上下文
 * @author: liyajie
 * @version: 1.0
 * @date: 2021/07/02 15:54
 */
@Component
public class MyContext {
    private static final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_PROVIDER_ID";
    private static final Map<String, Object> M_CONTEXT = new ConcurrentHashMap<>();

    public void setCurrentTenantId(Long tenantId) {
        M_CONTEXT.put(KEY_CURRENT_TENANT_ID, tenantId);
    }

    public Long getCurrentTenantId() {
        return (Long) M_CONTEXT.get(KEY_CURRENT_TENANT_ID);
    }
}
