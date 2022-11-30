package io.train.modules.business.leavemessage.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("course_leave_message")
public class LeaveMessageEntity implements Serializable {
	
	@TableId
	private String uuid;
	
	private float rate;
	
	private String courseTypeId;

	@TableField(exist = false)
	private String courseTypeName;

	@TableField(exist = false)
	private String courseTypeIntroduction;

	@TableField(exist = false)
	private String courseTypeIsRequired;

	@TableField(exist = false)
	private String courseType;
	
	private String courseId;

	@TableField(exist = false)
	private String courseName;

	@TableField(exist = false)
	private String courseStatus;

	@TableField(exist = false)
	private String courseCover;
	
	private String content;
	
	private String creater;
	
	private String createrId;
	
	private Date createTime;
	
	private String deleteFlag;
	
	private String parentUuid;
	
	private int isPrivate;

	@TableField(exist = false)
	private List<LeaveMessageEntity> childrenList;

	@TableField(exist = false)
	private Integer num;

}
