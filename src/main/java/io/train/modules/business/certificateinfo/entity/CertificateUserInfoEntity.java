package io.train.modules.business.certificateinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:51:01
 */
@Data
@TableName("certificate_student_info")
public class CertificateUserInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;
	/**
	 * 证书id
	 */
	private String certificateUuid;

	/**
	 * 证书名称
	 */
	private String certUuidLabel;
	/**
	 * 学生id
	 */
	private String userId;
	/**
	 * 创建人
	 */
	private String creater;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 删除标识
	 */
	private String deleteFlag;
	/**
	 * 课程id
	 */
	@TableField(exist = false)
	private String courseId;

}
