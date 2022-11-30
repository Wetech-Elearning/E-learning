package io.train.modules.business.organization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:42:16
 */
@Data
@TableName("organization")
public class OrganizationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long uuid;
	/**
	 *
	 */
	private String orgName;
	/**
	 *
	 */
	private String orgType;
	/**
	 *
	 */
	private String parentOrg;
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
	 * 子组织
	 **/
	@TableField(exist = false)
	private List<OrganizationEntity> children;

}
