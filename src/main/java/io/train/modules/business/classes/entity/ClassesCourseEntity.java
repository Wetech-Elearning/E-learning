package io.train.modules.business.classes.entity;

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
 * @date 2021-07-05 11:24:08
 */
@Data
@TableName("class_course")
public class ClassesCourseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;
	/**
	 * 
	 */
	private String classId;
	
	/**
	 * 
	 */
	private String courseType;
	
	/**
	 * 
	 */
	private String courseTypeLabel;
	/**
	 * 
	 */
	private String courseId;
	
	/**
	 * 
	 */
	private String couseName;
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
	private String deleteFlag;

}
