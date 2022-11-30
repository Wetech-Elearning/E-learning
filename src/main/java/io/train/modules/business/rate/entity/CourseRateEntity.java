package io.train.modules.business.rate.entity;

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
 * @date 2021-09-07 22:47:05
 */
@Data
@TableName("course_rate")
public class CourseRateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;
	/**
	 * 课程id
	 */
	private String courseId;
	/**
	 * 评分
	 */
	private Integer rate;
	/**
	 * 创建人
	 */
	private String creater;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 租户id
	 */
	private Integer tenantId;
	/**
	 * 备注
	 */
	private String remark;

}
