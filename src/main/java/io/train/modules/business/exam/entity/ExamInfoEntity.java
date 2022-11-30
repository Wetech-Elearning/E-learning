package io.train.modules.business.exam.entity;

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
@TableName("exam_info")
public class ExamInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@TableId
	private Long uuid;
	/**
	 * 试题名称
	 */
	private String examQuestionName;
	/**
	 * 归属试卷
	 */
	private String relatedExamId;
	/**
	 * 试题类型
	 */
	private Integer questionType;
	/**
	 * 选项A
	 */
	private String aOption;
	/**
	 * 选项B
	 */
	private String bOption;
	/**
	 * 选项C
	 */
	private String cOption;
	/**
	 * 选项D
	 */
	private String dOption;
	/**
	 * 判断对勾
	 */
	private Integer greenOption;
	/**
	 * 判断X号
	 */
	private Integer redOption;
	/**
	 * 正确答案
	 */
	private String correctOption;

}
