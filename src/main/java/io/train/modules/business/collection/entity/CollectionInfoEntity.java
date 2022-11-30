package io.train.modules.business.collection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2021-07-05 10:12:46
 */
@Data
@TableName("collection_info")
public class CollectionInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long uuid;
	/**
	 * 学员id
	 */
	private String userId;
	
	private String userName;
	
	private String courseType;
	/**
	 * 课程id
	 */
	private String courseId;
	
	private String courseName;
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

}
