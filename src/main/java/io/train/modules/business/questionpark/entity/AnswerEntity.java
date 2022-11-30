package io.train.modules.business.questionpark.entity;

import static com.baomidou.mybatisplus.annotation.IdType.UUID;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("question_park_answer")
public class AnswerEntity implements Serializable {
	
	@TableId(type = UUID)
	private String uuid;

	private String questionParkUuid;
	
	private String content;
	
	private String creater;
	
	private String createTime;
	
	private String delFlag;

}
