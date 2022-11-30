package io.train.modules.business.coursetype.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.train.modules.business.course.entity.CourseEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:40:30
 */
@Data
@TableName("course_type")
public class CourseTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;

	private String parentCourseType;
	/**
	 *
	 */
	private String courseTypeName;
	/**
	 *
	 */
	private String type;
	/**
	 *
	 */
	private String courseCover;
	/**
	 *
	 */
	private Integer level;
	/**
	 *
	 */
	private String courseTypeIntroduction;
	/**
	 *
	 */
	private String creater;


	private String certificateId;

	private String certificateName;
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
	 *
	 */
	private String deleteFlag;

	/**
	 *是否收藏
	 */
	@TableField(exist = false)
	private boolean collectFlag;

	@TableField(exist = false)
	private List<CourseTypeEntity> childrenCourseTypeEntity;

	@TableField(exist = false)
	private List<CourseEntity> listCourseEntity;

	@TableField(exist = false)
	private List<CourseTypeEntity> children;

}
