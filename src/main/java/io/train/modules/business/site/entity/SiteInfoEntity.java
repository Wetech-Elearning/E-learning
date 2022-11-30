package io.train.modules.business.site.entity;

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
 * @date 2021-07-05 10:55:23
 */
@Data
@TableName("site_info")
public class SiteInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@TableId
	private Long uuid;
	/**
	 * 发信人
	 */
	private String sendUserId;
	/**
	 * 发信名称
	 */
	@TableField(exist = false)
	private String sendUserName;
	/**
	 * 发信内容
	 */
	private String content;
	/**
	 * 是否是公司全体员工
	 */
	private Integer isAllCompany;
	/**
	 * 收信人类型
	 */
	private Integer type;
	/**
	 * 收信人
	 */
	private String acceptUserId;
	/**
	 * 收信人名称
	 */
	@TableField(exist = false)
	private String acceptUserName;
	/**
	 * 发信时间
	 */
	private Date sendDate;
	/**
	 * 失效时间
	 */
	private Date invalidDate;

	/**
	 * 删除标识
	 */
	private String  deleteFlag;

}
