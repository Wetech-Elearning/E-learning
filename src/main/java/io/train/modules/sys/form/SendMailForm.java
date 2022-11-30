package io.train.modules.sys.form;

import lombok.Data;

/**
 * 忘记密码发送邮箱form
 * @author: 李亚杰
 * @version: 1.0
 * @date: 2021/11/4 10:11
 */
@Data
public class SendMailForm {
    private String account;
    private String mail;
}
