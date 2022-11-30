package io.train.modules.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改用户状态dto
 * @author:liyajie
 * @createTime:2022/5/21 19:42
 * @version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserStatusDto {
    /**
     * 用户名称
     **/
    private String username;
    /**
     * 用户状态
     **/
    private Integer status;
}
