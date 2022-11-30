package io.train.common.aspect;

import io.train.common.lang.StringUtils;
import io.train.config.MyContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/8/10 0:01
 */
@Aspect
@Configuration
public class TenantAspect {
    @Autowired
    MyContext myContext;

    @Pointcut("execution(public * io.train.modules.business.common.*.controller.*.*(..)) || execution(* io.train.modules.sys.controller.*.*(..)) || execution(* io.train.modules.job.controller.*.*(..))")
    public void poinCut() {
    }

    @Before("poinCut()")
    public void before(JoinPoint point) throws Throwable {
        try{
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();
            String tenantId = request.getHeader("tenantId");
            if(StringUtils.isNotBlank(tenantId) && StringUtils.isNotEmpty(tenantId) && !"null".equals(tenantId)){
                myContext.setCurrentTenantId(Long.parseLong(tenantId));
            }
            //超级管理员添加企业管理员时，租户id取值所属企业
            /*MethodSignature methodSignature = (MethodSignature) point.getSignature();
            String classPath = methodSignature.getMethod().getDeclaringClass().getName();
            String methodName = methodSignature.getMethod().getName();
            if("io.train.modules.business.common.company.controller.CompanyController.save".equals(classPath + "." + methodName)){
                CompanyEntity companyEntity = (CompanyEntity)point.getArgs()[0];
                myContext.setCurrentTenantId(companyEntity.getUuid());
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 转换request 请求参数
     * @author: liyajie
     * @date: 2021/7/9 10:33
     * @param paramMap-请求参数
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @exception:
     * @update:
     * @updatePerson:
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }
}
