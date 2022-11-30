package io.train.common.persistence;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.train.common.utils.IdGen;
import io.train.common.utils.ShiroUtils;
import io.train.modules.sys.entity.SysUserEntity;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

public abstract class DataEntity<T> extends BaseEntity<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8792493920040524482L;
	protected String remarks; // 备注
	protected String createBy; // 创建者
	protected Date createDate; // 创建日期
	protected String updateBy; // 更新者
	protected Date updateDate; // 更新日期
	@TableLogic
	protected String delFlag; // 删除标记（0：正常；1：删除）

	public DataEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}

	public DataEntity(String id) {
		super(id);
	}

	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	protected SysUserEntity getUser() {
		return ShiroUtils.getUserEntity();
	}

	/**
	 * 获取用户id
	 * 
	 * @return
	 */
	protected String getUserId() {
		return String.valueOf(getUser().getUserId());
	}

	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert() {
		// 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
		if (!this.isNewRecord) {
			setId(IdGen.uuid());
		}
		if (getUser() != null) {
			this.updateBy = getUserId();
			this.createBy = this.updateBy;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}

	/**
	 * 更新之前执行方法，需要手动调用
	 */
	@Override
	public void preUpdate() {
		if (getUser() != null) {
			this.updateBy = getUserId();
		}
		this.updateDate = new Date();
	}

	@Length(min = 0, max = 255)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}