package io.train.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 注册表单
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "注册表单")
public class RegisterForm {
    @ApiModelProperty(value = "手机号")
    @NotBlank(message="手机号不能为空")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message="邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "密码")
    @NotBlank(message="密码不能为空")
    private String password;

    @ApiModelProperty(value = "所属公司")
    @NotBlank(message="所属公司不能为空")
    private String company;

    @ApiModelProperty(value = "激活码")
    private String code;

}
