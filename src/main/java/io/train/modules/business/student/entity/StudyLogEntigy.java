package io.train.modules.business.student.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:44:03
 */
@Data
@TableName("study_log")
public class StudyLogEntigy implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String uuid;
	
	private String courseId;
	
	private String userId;
	
	private long time;
	
	private long totalTime;
	
	private String isFished;
	
	private String deleteFlag;

}
