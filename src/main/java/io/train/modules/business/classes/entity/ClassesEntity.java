package io.train.modules.business.classes.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.student.entity.StudentEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 11:24:08
 */
@Data
@TableName("classes")
public class ClassesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long uuid;
	/**
	 * 
	 */
	private String className;
	/**
	 * 
	 */
	private Date startDate;
	/**
	 * 
	 */
	private Date endDate;
	/**
	 * 
	 */
	private Integer status;
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
	
	
	@TableField(exist = false)
	private List<StudentEntity> studentEntityList;
	
	@TableField(exist = false)
	private List<CourseTypeEntity> courseEntityList;

}
