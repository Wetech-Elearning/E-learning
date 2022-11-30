package io.train.modules.business.translate.entity;

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
 * @date 2021-08-30 23:55:05
 */
@Data
@TableName("translate")
public class TranslateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@TableId
	private Long uuid;
	/**
	 * 中文
	 */
	private String chinese;
	/**
	 * 日文
	 */
	private String japanese;
	/**
	 * 课程id
	 */
	private String relatedCourseId;
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
	 * 删除标识
	 */
	private String deleteFlag;


}
