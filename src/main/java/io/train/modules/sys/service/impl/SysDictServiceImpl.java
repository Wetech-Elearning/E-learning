package io.train.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.sys.dao.SysDictDao;
import io.train.modules.sys.entity.SysDictEntity;
import io.train.modules.sys.service.SysDictService;

@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {

		IPage<SysDictEntity> page = this.page(
				new Query<SysDictEntity>().getPage(params),
				new QueryWrapper<SysDictEntity>().orderBy(true,true,"sort_no")
		);
		return new PageUtils(page);
	}

	/**
	 * 新增或修改数据字典信息
	 *
	 * @param sysDict
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveOrUpdate(SysDictEntity sysDict) {
		// TODO Auto-generated method stub
		String parentIds = "";
		SysDictEntity parent = this.getById(sysDict.getParentId());
		if (parent == null) {
			sysDict.setParentId(SysDictEntity.SYS_DICT_ROOT);
			parentIds = SysDictEntity.SYS_DICT_ROOT;
		} else {
			parentIds = parent.getParentIds() + "," + sysDict.getParentId();
		}
		sysDict.setParentIds(parentIds);

		if (StringUtils.isBlank(sysDict.getId())) {
			sysDict.preInsert();
			return this.save(sysDict);
		} else {
			sysDict.preUpdate();
			return this.updateById(sysDict);
		}
	}

	/**
	 * 删除数据字典项，如果有子集一起删除
	 *
	 * @param id
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteObjAndChildren(String id) {
		// TODO Auto-generated method stub
		SysDictEntity sysDict = this.getById(id);
		if (sysDict != null) {
			// 首先删除所有子集元素
			this.remove(new QueryWrapper<SysDictEntity>().like(true,"parent_ids", sysDict.getParentIds() + "," + sysDict.getId()));
			// 再删除元素自身
			this.removeById(sysDict.getId());
		}
	}

	/**
	 * 根据数据字典code获取包含数据字典子项列表
	 *
	 * @param dictCode
	 * @return
	 */
	@Override
	public List<SysDictEntity> findChildrenByCode(String dictCode) {
		// TODO Auto-generated method stub
		SysDictEntity sysDict = this.getOne(new QueryWrapper<SysDictEntity>().eq("dict_code", dictCode));
		if (sysDict != null) {
			return this.list(
					new QueryWrapper<SysDictEntity>().eq("parent_id", sysDict.getId()).orderBy(true, true,"sort_no"));
		}
		return new ArrayList<SysDictEntity>();
	}

	/**
	 * 获取所有数据字典项列表数据
	 *
	 * @return
	 */
	@Override
	public List<SysDictEntity> findAllDictList() {
		// TODO Auto-generated method stub
		List<SysDictEntity> sysDictList = this
				.list(new QueryWrapper<SysDictEntity>().orderBy(true, true,"parent_ids").orderBy(true, true,"sort_no"));
		return sysDictList;
	}

	/**
	 * 根据数据字典code获取包含数据字典子项map
	 *
	 * @param dictCode
	 * @return
	 */
	@Override
	public Map<String, String> findMap(String dictCode) {
		// TODO Auto-generated method stub
		Map<String, String> dictMap = new HashMap<String, String>();
		List<SysDictEntity> list = this.findChildrenByCode(dictCode);
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysDictEntity sysDict : list) {
				dictMap.put(StringUtils.trimToEmpty(sysDict.getDictCode()),
						StringUtils.trimToEmpty(sysDict.getDictName()));
			}
		}
		return dictMap;
	}

	/**
	 * 根据数据字典code获取包含数据字典子项map
	 *
	 * @param dictCode
	 * @return
	 */
	@Override
	public Map<String, String> findMapByDictCode(String dictCode) {
		// TODO Auto-generated method stub
		Map<String, String> dictMap = new HashMap<String, String>();
		List<SysDictEntity> list = this.baseMapper.selectList(new QueryWrapper<SysDictEntity>().eq("dict_code", dictCode));
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysDictEntity sysDict : list) {
				dictMap.put(StringUtils.trimToEmpty(sysDict.getDictCode()),
						StringUtils.trimToEmpty(sysDict.getDictName()));
			}
		}
		return dictMap;
	}
}