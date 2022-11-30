package io.train.modules.business.comment.entity;

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
 * @date 2021-07-05 10:49:41
 */
@Data
@TableName("comment")
public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * uuid
	 */
	@TableId
	private Long uuid;
	/**
	 * 学员id
	 */
	private String userId;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论课程id
	 */
	private String courseId;
	/**
	 * 评论时间
	 */
	private Date createDate;
	/**
	 * 评论人
	 */
	private String creater;

}
