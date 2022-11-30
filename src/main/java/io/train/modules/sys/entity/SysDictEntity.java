package io.train.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.train.common.persistence.TreeEntity;

import static com.baomidou.mybatisplus.annotation.IdType.UUID;

/**
 * 数据字典信息表
 * 
 * @author Tony
 * @email
 * @date 2019-07-23 15:55:11
 */
@TableName("sys_dict")
public class SysDictEntity extends TreeEntity<SysDictEntity> {
	private static final long serialVersionUID = 9069157172314917615L;
	public static final String SYS_DICT_ROOT = "0";

	@TableId(type = UUID)
	private String id;
	// 字典名称
	private String dictName;
	// 别名
	private String aliasName;
	// 字典编码
	private String dictCode;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

}
