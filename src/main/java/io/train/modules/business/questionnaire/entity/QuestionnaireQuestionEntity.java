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
 * @date 2021-10-24 04:53:55
 */
@Data
@TableName("questionnaire_question")
public class QuestionnaireQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;

	/**
	 *问卷题目类型
	 */
	private String questionType;
	
	/**
	 * 
	 */
	private String question;

	/**
	 * 选项A
	 */
	private String answerA;
	/**
	 * 选项B
	 */
	private String answerB;
	/**
	 * 选项C
	 */
	private String answerC;
	/**
	 * 选项D
	 */
	private String answerD;
	/**
	 * 
	 */
	private String creater;
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
	private Date updateDate;
	/**
	 * 
	 */
	private String remark;
	/**
	 * 
	 */
	private String deleteFlag;

}
