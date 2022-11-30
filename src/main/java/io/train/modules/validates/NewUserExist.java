package io.train.modules.validates;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.List;
import java.util.Objects;

/**
 * 校验用户账户是否存在
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/9/1 23:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Constraint(validatedBy = NewUserExist.UserExistValidator.class)
public @interface NewUserExist {
    // 校验未通过时的返回信息
    String message() default "用户账户已存在";

    // 以下两行为固定模板
    Class<?>[] groups() default {};

    // 约束注解的有效负载
    Class<? extends Payload>[] payload() default {};

    class UserExistValidator implements ConstraintValidator<NewUserExist, String> {

        @Autowired
        SysUserService sysUserService;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<SysUserEntity>();
            queryWrapper.eq("username",value);
            List<SysUserEntity> sysUserEntityList = sysUserService.list(queryWrapper);
            if(null != sysUserEntityList && sysUserEntityList.size() > 0){
                return false;
            }
            return true;
        }
    }
}

