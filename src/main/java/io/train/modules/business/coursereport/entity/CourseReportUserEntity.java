package io.train.modules.business.coursereport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-08-17 17:16:04
 */
@Data
@TableName("course_report_user")
public class CourseReportUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 学员id
	 */
	private String userId;
	/**
	 * 试卷id
	 */
	private Long courseReportId;
	/**
	 * 租户id
	 */
	private Integer tenantId;

}
