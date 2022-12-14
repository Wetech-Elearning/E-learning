package io.train.common.persistence;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public abstract class BaseEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 实体编号（唯一标识）
	 */
	@TableId
	protected String id;

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	@TableField(exist = false)
	protected boolean isNewRecord = false;

	public BaseEntity() {

	}

	public BaseEntity(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 插入之前执行方法，子类实现
	 */
	public abstract void preInsert();

	/**
	 * 更新之前执行方法，子类实现
	 */
	public abstract void preUpdate();

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean getIsNewRecord() {
		return isNewRecord || getId() != null;
	}

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	public void setIsNewRecord(boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
	}

	/**
	 * 删除标记（0：正常；1：删除）
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
}