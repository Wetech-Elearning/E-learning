package io.train.modules.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户个人信息Dto
 * @author:liyajie
 * @createTime:2022/6/1 17:56
 * @version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    /**
     * 用户名称
     **/
    private String username;
    /**
     * 用户邮箱
     **/
    private String email;
    /**
     * 用户手机
     **/
    private String mobile;
    /**
     * 用户状态
     **/
    private String status;
    /**
     * 用户头像
     **/
    private String avatar;
    /**
     * 用户简介
     **/
    private String introduction;
}
