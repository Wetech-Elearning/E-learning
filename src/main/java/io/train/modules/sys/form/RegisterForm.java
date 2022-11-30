package io.train.modules.sys.form;

import lombok.Data;

/***
 * 注册表单
 * @author: liyajie
 * @version: 1.0
 * @date: 2021/07/15 23:27
 */
@Data
public class RegisterForm {
    private String company;
    private String surname;
    private String username;
    private String account;
    private String password;
    private String email;
    private String mobile;
    private String captcha;
    private String uuid;
    private String sex;
    private String department;
    private String office;
    private Long tenantId;
}
