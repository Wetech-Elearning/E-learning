package io.train.modules.business.home.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.train.modules.business.coursetype.entity.CourseTypeEntity;

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
@TableName("render_home_manager")
public class RenderHomeManager implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 
	 */
	private String renderId;
	/**
	 * 
	 */
	private String  logoName;
	/**
	 * 
	 */
	private String logoImage;
	/**
	 * 
	 */
	private String carouselList;
	/**
	 * 
	 */
	private String hotCourseTypeList;
	/**
	 * 
	 */
	private String pubCourseTypeList;
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
	private String creator;
	/**
	 * 
	 */
	private Date updateDate;
	
	/**
	 * 删除标识
	 */
	private String  deleteFlag;
	@TableField(exist = false)
	private List<CourseTypeEntity> hotList;
	@TableField(exist = false)
	private List<CourseTypeEntity> pubList;
	

}