package io.train.modules.business.record.entity;

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
 * @date 2021-07-05 10:43:20
 */
@Data
@TableName("student_study_record")
public class StudentStudyRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long uuid;
	/**
	 * 
	 */
	private String userId;
	/**
	 * 
	 */
	private String courseId;
	/**
	 * 
	 */
	private String learnTime;
	/**
	 * 
	 */
	private String courseTotalTime;
	
	private String onlineTime;
	
	private int isFinished;
	
	private Date dateTime;
	
	/**
	 * 
	 */
	private String deleteFlag;

}