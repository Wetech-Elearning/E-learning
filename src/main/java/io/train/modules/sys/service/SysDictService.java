package io.train.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.sys.entity.SysDictEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据字典信息表
 *
 * @author Tony
 * @email
 * @date 2019-07-23 15:55:11
 */
public interface SysDictService extends IService<SysDictEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 新增或修改数据字典信息
	 * 
	 * @param sysDict
	 */
	boolean saveOrUpdate(SysDictEntity sysDict);

	/**
	 * 删除数据字典项，如果有子集一起删除
	 * 
	 * @param id
	 * @return
	 */
	void deleteObjAndChildren(String id);

	/**
	 * 根据数据字典code获取包含数据字典子项列表
	 * 
	 * @param dictCode
	 * @return
	 */
	List<SysDictEntity> findChildrenByCode(String dictCode);

	/**
	 * 获取所有数据字典项列表数据
	 * 
	 * @return
	 */
	List<SysDictEntity> findAllDictList();
	
	/**
	 * 根据数据字典code获取包含数据字典子项map
	 * 
	 * @param dictCode
	 * @return
	 */
	Map<String, String> findMap(String dictCode);

	/**
	 * 根据dictCode查询dictName
	 * @author: liyajie
	 * @date: 2021/8/30 22:03
	 * @param dictCode
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @exception:
	 * @update:
	 * @updatePerson:
	 **/
	Map<String, String> findMapByDictCode(String dictCode);
}
