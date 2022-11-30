package io.train.modules.business.company.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2021-07-05 10:12:46
 */
@Data
@TableName("company")
public class CompanyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private String uuid;
	/**
	 *
	 */
	private String companyName;
	/**
	 *
	 */
	private String companyIntroduction;
	/**
	 *
	 */
	private String companyContact;
	/**
	 *
	 */
	private String companyEmail;
	/**
	 *
	 */
	private String companyLocation;
	/**
	 *
	 */
	private String companyNature;
	/**
	 *
	 */
	private String companyLegalPerson;
	/**
	 *
	 */
	private String companyMobile;
	/**
	 *
	 */
	private Date createDate;
	/**
	 *
	 */
	private Date updateDate;
	/**
	 *
	 */
	private String creater;
	/**
	 *
	 */
	private String updater;
	/**
	 *
	 */
	private Integer status;
	/**
	 * 删除标识
	 */
	private String  deleteFlag;
	/**
	 *
	 */
	private String remark;

}
