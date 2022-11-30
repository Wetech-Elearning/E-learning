package io.train.modules.business.relation.entity;

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
 * @date 2021-07-05 10:55:17
 */
@Data
@TableName("user_exam_info")
public class UserExamInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@TableId
	private Long uuid;
	/**
	 * 学员id
	 */
	private Long userId;
	/**
	 * 课程id
	 */
	private Long courseId;
	/**
	 * 试卷id
	 */
	private Long examPaperId;
	/**
	 * 分数
	 */
	private String score;
	/**
	 * 考试开始时间
	 */
	private Date startDate;
	/**
	 * 考试结束时间
	 */
	private Date endDate;
	/**
	 * 考试是否合格
	 */
	private Integer isPass;
	/**
	 * 是否考试
	 */
	private Integer isExam;
	/**
	 * 考试次数
	 */
	private Integer examNum;

}
