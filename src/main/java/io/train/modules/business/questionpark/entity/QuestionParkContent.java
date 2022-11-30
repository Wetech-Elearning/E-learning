package io.train.modules.business.questionpark.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.UUID;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:41:24
 */
@Data
@TableName("question_park_content")
public class QuestionParkContent implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = UUID)
	private String uuid;
	
	private String parentUuid;

	private String title;
	
	private String createrId;
	
	private String creater;
	
	private String content;
	
	private Date createTime;
	
	private String deleteFlag;
	
}
