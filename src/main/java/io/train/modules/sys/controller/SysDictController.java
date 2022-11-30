package io.train.modules.sys.controller;

import io.train.common.annotation.SysLog;
import io.train.common.utils.R;
import io.train.modules.sys.entity.SysDictEntity;
import io.train.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据字典信息管理
 *
 * @author Tony
 * @email
 * @date 2019-07-23 15:55:11
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController extends BaseController {
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 获取数据字典项数据
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		List<SysDictEntity> list = sysDictService.findAllDictList();
		return R.ok().put("data", list);
	}

	/**
	 * 获取数据字典详细信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") String id) {
		SysDictEntity sysDict = sysDictService.getById(id);
		return R.ok().put("data", sysDict);
	}

	/**
	 * 新增数据字典项
	 * 
	 * @param sysDict
	 * @return
	 */
	@SysLog("新增数据字典")
	@RequestMapping("/save")
	public R save(@RequestBody SysDictEntity sysDict) {
		sysDictService.saveOrUpdate(sysDict);
		return R.ok();
	}

	/**
	 * 修改数据字典项
	 * 
	 * @param sysDict
	 * @return
	 */
	@SysLog("修改数据字典")
	@RequestMapping("/update")
	public R update(@RequestBody SysDictEntity sysDict) {
		sysDictService.saveOrUpdate(sysDict);
		return R.ok();
	}

	/**
	 * 删除数据字典项，如果有子集一起删除
	 * 
	 * @param id
	 * @return
	 */
	@SysLog("删除数据字典")
	@RequestMapping("/delete/{id}")
	public R delete(@PathVariable String id) {
		sysDictService.deleteObjAndChildren(id);
		return R.ok();
	}

	/**
	 * 根据数据字典code获取包含数据字典子项列表
	 * 
	 * @param dictCode
	 * @return
	 */
	@RequestMapping("/json/data/{dictCode}")
	public R getDictData(@PathVariable String dictCode) {
		List<SysDictEntity> list = sysDictService.findChildrenByCode(dictCode);
		return R.ok().put("data", list);
	}

	/**
	 * 获取数据字典列表
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/select")
	public R select() {
		List<SysDictEntity> list = sysDictService.findAllDictList();
		SysDictEntity root = new SysDictEntity();
		root.setId(SysDictEntity.SYS_DICT_ROOT);
		root.setDictName("一级字典");
		root.setParentId("");
		list.add(root);
		return R.ok().put("data", list);
	}

}
