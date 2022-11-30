package io.train.modules.business.relation.entity;

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
 * @date 2021-07-05 10:48:33
 */
@Data
@TableName("class_course")
public class ClassCourseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long classId;
	/**
	 * 
	 */
	private Long courseType;
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
