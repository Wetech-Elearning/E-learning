package io.train.common.persistence;


import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.List;

/**
 * 权限控制
 * @author zqdpc
 *
 */
public abstract class PermissionEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@TableField(exist=false)
	protected List<?> deptIds;	//权限控制

	public List<?> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(List<?> deptIds) {
		this.deptIds = deptIds;
	}
	
}
