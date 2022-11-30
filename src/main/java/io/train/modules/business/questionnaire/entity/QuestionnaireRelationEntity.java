package io.train.modules.business.questionnaire.entity;

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
 * @date 2021-10-25 00:14:56
 */
@Data
@TableName("questionnaire_relation")
public class QuestionnaireRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String uuid;
	/**
	 * 问卷id
	 */
	private String questionnaireId;
	/**
	 * 问卷名称
	 */
	@TableField(exist = false)
	private String questionnaireName;
	/**
	 * 接收者
	 */
	private String acceptUserId;
	/**
	 * 状态：0：未处理，1：已处理
	 */
	private Integer status;
	/**
	 * 删除标识
	 */
	private String deleteFlag;

}
