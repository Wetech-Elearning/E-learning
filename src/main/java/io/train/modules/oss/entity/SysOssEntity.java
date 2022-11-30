package io.train.modules.oss.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_oss")
public class SysOssEntity implements Serializable {

	@TableId
	private Long id;
	private String title;
	//文件原始名称
	private String name;
	//云服务器上文件原始名称
	private String ossFileName;
	//URL地址
	private String url;
	//后缀
	private String suffix;
	//文件id
	private Long objId;
	//文件类型
	private String objType;
	//文件大小
	private Integer fileSize;
	//排序
	private Integer sortNo;
	//文件上传进度百分比
	private Integer percent;
	//创建者
	private String creater;
	//创建时间
	private Date createDate;

}
