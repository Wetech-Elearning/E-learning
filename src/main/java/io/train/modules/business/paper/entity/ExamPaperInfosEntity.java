package io.train.modules.business.paper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.*;

import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:51:50
 */
@Data
@TableName("exam_paper_infos")
public class ExamPaperInfosEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String  uuid;
	/**
	 * 试卷名称
	 */
	private String examPaperName;
	/**
	 * 试卷状态
	 */
	private String status;
	/**
	 * 课程名称
	 */
	private String subject;
	/**
	 * 课程id
	 */
	@TableField(exist = false)
	private String subjectId;
	/**
	 * 试卷描述
	 */
	private String description;
	/**
	 * 试卷总分
	 */
	private String totalScore;
	/**
	 * 试卷及格分
	 */
	private String passScore;
	/**
	 * 考试时长阈值
	 */
	private String  exmaTimeLimit;
	/**
	 * 试卷考试最大次数
	 */
	private Integer examMaxNum;
	/**
	 * 创建人
	 */
	private String creater;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改人
	 */
	private String updater;
	/**
	 * 修改时间
	 */
	private Date updateDate;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 删除标识
	 */
	private String deleteFlag;

	@TableField(exist = false)
	private Map examPaperDetail = new HashMap();
	
	@TableField(exist = false)
	private float maxScore;
	
	@TableField(exist = false)
	private int examNum;

	@TableField(exist = false)
	private int answerScore;

}
