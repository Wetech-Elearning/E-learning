package io.train.modules.business.coursereport.entity;

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
 * @date 2022-08-09 14:37:08
 */
@Data
@TableName("course_report")
public class CourseReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 二级课程包id
	 */
	private Long courseTypeId;
	/**
	 * 二级课程包名称
	 */
	@TableField(exist = false)
	private String courseTypeName;
	/**
	 * 课程报告题目
	 */
	private String courseReportTitle;
	/**
	 * 课程报告题目
	 */
	private String courseReportContent;
	/**
	 * 发布状态
	 */
	private String status;
	/**
	 * 提交次数
	 */
	private Integer submitNums;
	/**
	 * 分数
	 */
	private String score;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 截止时间
	 */
	private Date deadlineDate;
	/**
	 * 备注
	 */
	private String remark;

}
