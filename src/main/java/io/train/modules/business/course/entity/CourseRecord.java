package io.train.modules.business.course.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("student_course_record")
public class CourseRecord implements Serializable{
	
	private String courseId;
	
	private String userId;
	
	private String totalTime;
	
	private String currentTime;
	
	private String isFinished;
	
}
