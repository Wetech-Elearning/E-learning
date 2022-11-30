package io.train.modules.business.course.entity;

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
 * @date 2021-07-05 10:39:34
 */
@Data
@TableName("course")
public class CourseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 课程名称
	 */
	private String courseName;
	/**
	 * 课程简介
	 */
	private String courseIntroduction;
	/**
	 * 课程归属课程包ID
	 */
	private String relatedCourseTypeId;
	/**
	 * 课程归属课程包
	 */
	private String relatedCourseType;
	/**
	 * 课程序号
	 */
	private Integer serial;
	/**
	 * 课程类型
	 */
	private String fileType;

	/**
	 *
	 */
	private String fileId;
	/**
	 * 课程url
	 */
	private String fileUrl;
	/**
	 * 课程总时长
	 */
	private String totalTime;
	/**
	 * 试卷ID
	 */
	private String examPaperId;

	/**
	 * 试卷名称
	 */
	private String examPaperName;
	/**
	 * 课程发布状态
	 */
	private String status;
	/**
	 *
	 */
	private String statusLabel;
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
	/**
	 * 证书id
	 */
	private String certificateId;
	/**
	 * 证书名称
	 */
	private String certificateName;

	/**
	 * 上传进度
	 */
	@TableField(exist = false)
	private int percent;

	@TableField(exist = false)
	private String readTime;

	@TableField(exist = false)
	private String onlineTime;

	@TableField(exist = false)
	private String isFinished;

}
