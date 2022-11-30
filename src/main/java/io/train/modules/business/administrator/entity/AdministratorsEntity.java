package io.train.modules.business.administrator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.train.common.validator.group.AddGroup;
import io.train.common.validator.group.UpdateGroup;
import io.train.modules.validates.NewUserExist;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:12:46
 */
@Data
@TableName("administrators")
public class AdministratorsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 姓氏
	 */
	private String surname;
	/**
	 * 用户
	 */
	private String userName;

	/**
	 * 账户
	 */
	@NotBlank(message="账户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@NewUserExist
	private String account;

	/**
	 * 
	 */
	private String userMobile;
	/**
	 * 
	 */
	private String userEmail;
	/**
	 * 
	 */
	private String relatedCompany;
	/**
	 * 
	 */
	private String sex;
	/**
	 * 
	 */
	private Integer age;
	/**
	 * 
	 */
	private String creater;
	/**
	 * 
	 */
	private Date createDate;
	/**
	 * 
	 */
	private String updater;
	/**
	 * 
	 */
	private Date updateDate;
	/**
	 * 
	 */
	private String remark;
	/**
	 * 删除标识
	 */
	private String  deleteFlag;
	/**
	 * 状态
	 */
	@TableField(exist = false)
	private Integer status;

}
