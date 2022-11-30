package io.train.modules.business.relation.entity;

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
 * @date 2021-09-12 16:20:26
 */
@Data
@TableName("user_exam_record")
public class UserExamRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;
	/**
	 * 学员id
	 */
	private String userId;
	/**
	 * 试卷id
	 */
	private String paperId;
	/**
	 * 试题类型
	 */
	private String questionType;
	/**
	 * 试题id
	 */
	private String questionId;
	/**
	 * 试题回答
	 */
	private String questionAnswer;
	/**
	 * 得分
	 */
	private String score;
	/**
	 * 当前的考试次数
	 */
	private int examNum;

}
