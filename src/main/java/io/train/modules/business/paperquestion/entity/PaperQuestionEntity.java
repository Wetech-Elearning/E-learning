package io.train.modules.business.paperquestion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:52:42
 */
@Data
@TableName("paper_question")
public class PaperQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 试卷id
	 */
	private String paperId;
	/**
	 * 试题id
	 */
	private String questionId;
	/**
	 * 试题名称
	 */
	private String question;
	/**
	 * 试题类型
	 */
	private String questionType;

}
