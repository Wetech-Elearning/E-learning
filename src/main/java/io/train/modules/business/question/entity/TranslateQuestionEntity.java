package io.train.modules.business.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2021-09-12 18:37:56
 */
@Data
@TableName("translate_question")
public class TranslateQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;
	/**
	 * 考试课程
	 */
	private String subject;
	/**
	 * 翻译类型
	 */
	private String type;
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
	private String deleteFlag;
	/**
	 * 学员的作答
	 */
	@TableField(exist = false)
	private String  userAnswer;
}
