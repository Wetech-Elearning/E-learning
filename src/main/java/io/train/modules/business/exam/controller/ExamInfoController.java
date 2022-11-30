package io.train.modules.business.exam.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.modules.business.exam.entity.ExamInfoEntity;
import io.train.modules.business.exam.service.ExamInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:52:42
 */
@RestController
@RequestMapping("generator/examinfo")
public class ExamInfoController {
    @Autowired
    private ExamInfoService examInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:exampaperinfos:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = examInfoService.queryPage(params);

        return R.ok().put("page", page);
    }
    
    @RequestMapping("/listall")
    public R listAll(@RequestParam Map<String, Object> params){
    	List<ExamInfoEntity> page = examInfoService.queryAll(params);
        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:exampaperinfos:info")
    public R info(@PathVariable("uuid") String uuid){
		ExamInfoEntity examInfo = examInfoService.getById(uuid);

        return R.ok().put("examInfo", examInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:exampaperinfos:add")
    public R save(@RequestBody ExamInfoEntity examInfo){
		examInfoService.save(examInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:exampaperinfos:update")
    public R update(@RequestBody ExamInfoEntity examInfo){
		examInfoService.updateById(examInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:exampaperinfos:delete")
    public R delete(@RequestBody String[] uuids){
		examInfoService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
