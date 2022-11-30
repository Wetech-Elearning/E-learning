package io.train.modules.business.lecturer.entity;

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
 * @date 2021-07-05 10:41:24
 */
@Data
@TableName("lecturer")
public class LecturerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 姓氏
	 */
	private String surname;
	/**
	 * 讲师名称
	 */
	private String lecturerName;
	/**
	 * 账户
	 */
	@NotBlank(message="账户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@NewUserExist
	private String account;
	/**
	 * 
	 */
	private String lecturerMobile;
	/**
	 *
	 */
	private String lecturerEmail;
	/**
	 * 
	 */
	private String lecturerIntroduction;
	/**
	 * 
	 */
	private String lecturerCertificate;
	/**
	 * 
	 */
	private String lecturerBook;
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
