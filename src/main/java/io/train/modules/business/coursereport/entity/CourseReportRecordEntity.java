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
@TableName("course_report_record")
public class CourseReportRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 课程报告id
	 */
	private Long courseReportId;
	/**
	 * 课程报告标题
	 */
	@TableField(exist = false)
	private String courseReportTitle;
	/**
	 * 二级课程包名称
	 */
	@TableField(exist = false)
	private String courseTypeName;
	/**
	 * 学员课程报告标题
	 */
	private String studentCourseReportTitle;
	/**
	 * 学员id
	 */
	private String studentId;
	/**
	 * 学员名称
	 */
	@TableField(exist = false)
	private String studentName;
	/**
	 * 课程报告内容
	 */
	private String content;
	/**
	 * 评语
	 */
	private String comment;
	/**
	 * 评分
	 */
	private String score;
	/**
	 * 阅读状态
	 */
	private String status;
	/**
	 * 附件
	 */
	private String file;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 备注
	 */
	private String remark;

}
