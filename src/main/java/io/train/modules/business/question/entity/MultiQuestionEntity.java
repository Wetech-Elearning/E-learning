package io.train.modules.business.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 选择题题库表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-28 00:05:00
 */
@Data
@TableName("multi_question")
public class MultiQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;
	/**
	 * 试卷编号
	 */
	private String questionid;
	/**
	 * 考试科目
	 */
	private String subject;
	/**
	 * 题目类型
	 */
	private String questionType;
	/**
	 * 问题题目
	 */
	private String question;
	/**
	 * 选项A
	 */
	private String answera;
	/**
	 * 选项B
	 */
	private String answerb;
	/**
	 * 选项C
	 */
	private String answerc;
	/**
	 * 选项D
	 */
	private String answerd;
	/**
	 * 正确答案
	 */
	private String answer;
	/**
	 * 题目解析
	 */
	private String analysis;
	/**
	 * 分数
	 */
	private Integer score;
	/**
	 * 所属章节
	 */
	private String section;
	/**
	 * 难度等级
	 */
	private String level;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 删除标识
	 */
	private String  deleteFlag;
	/**
	 * 学员的作答
	 */
	@TableField(exist = false)
	private String  userAnswer;
	
}
