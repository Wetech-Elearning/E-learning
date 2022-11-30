package io.train.modules.business.relation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:48:33
 */
@Data
@TableName("class_student")
public class ClassStudentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long classId;
	/**
	 * 
	 */
	private Long userId;
	/**
	 * 
	 */
	private String deleteFlag;

}
