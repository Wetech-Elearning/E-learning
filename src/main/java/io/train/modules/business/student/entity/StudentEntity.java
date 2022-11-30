package io.train.modules.business.student.entity;

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
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:44:03
 */
@Data
@TableName("student")
public class StudentEntity implements Serializable {
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
	 * 名字
	 */
	private String userName;
	/**
	 * 账户
	 */
	@NotBlank(message="账户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@NewUserExist
	private String account;
	/**
	 * 电话
	 */
	private String userMobile;
	/**
	 *邮箱
	 */
	private String userEmail;
	/**
	 *所属公司
	 */
	private String relatedCompany;
	/**
	 * 所属部门
	 */
	private String relatedDepartment;
	/**
	 * 所属科室
	 */
	private String relatedOffice;
	/**
	 *入职年数
	 */
	private String employmentTime;
	/**
	 *已培训科目
	 */
	private String subjects;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 创建人
	 */
	private String creater;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改人
	 */
	private String updater;
	/**
	 * 修改时间
	 */
	private Date updateDate;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态
	 */
	@TableField(exist = false)
	private Integer  status;
	/**
	 * 删除标识
	 */
	private String  deleteFlag;
}
