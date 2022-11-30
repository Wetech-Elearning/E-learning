package io.train.common.persistence;

import com.baomidou.mybatisplus.annotation.TableField;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public abstract class TreeEntity<T> extends DataEntity<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5923031666099797506L;
	public static final String DEFAULT_ROOT_ID = "0"; // 默认父级根节点Id
	public static final String NODE_STATUS_NORMAL = "0"; // 正常状态
	public static final String NODE_STATUS_DISABLE = "1"; // 禁用状态

	protected String parentId; // 父级Id
	protected String parentName; // 父级名称
	protected String parentIds; // 所有父级编号
	protected String hasChildren; // 是否含有子集节点 0：不含有 1：含有
	protected String status; // 节点状态0：正常 1:禁用
	protected Integer sortNo; // 排序

	@TableField(exist = false)
	protected T parent; // 父级

	@TableField(exist = false)
	protected List<T> childList; // 包含子集

	public TreeEntity() {
		super();
		this.sortNo = 1;
	}

	public TreeEntity(String id) {
		super(id);
	}

	@Length(min = 1, max = 32)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Length(min = 1, max = 100)
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Length(min = 1, max = 1)
	public String getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	@Length(min = 1, max = 2000)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min = 1, max = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}

	public List<T> getChildList() {
		return childList;
	}

	public void setChildList(List<T> childList) {
		this.childList = childList;
	}

}