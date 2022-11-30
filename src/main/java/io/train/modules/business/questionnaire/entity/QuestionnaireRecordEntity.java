package io.train.modules.business.questionnaire.entity;

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
 * @date 2021-10-25 00:14:56
 */
@Data
@TableName("questionnaire_record")
public class QuestionnaireRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 问卷id
	 */
	private String questionnaireId;
	/**
	 * 问卷题目id
	 */
	private String questionnaireQuestionId;
	/**
	 * 问卷题目回答
	 */
	private String questionnaireQuestionAnswer;

}
