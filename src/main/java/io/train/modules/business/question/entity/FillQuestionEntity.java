package io.train.modules.business.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 填空题题库
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-28 00:05:00
 */
@Data
@TableName("fill_question")
public class FillQuestionEntity implements Serializable {
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
	 * 试题内容
	 */
	private String question;
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
	 * 难度等级
	 */
	private String level;
	/**
	 * 所属章节
	 */
	private String section;
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
