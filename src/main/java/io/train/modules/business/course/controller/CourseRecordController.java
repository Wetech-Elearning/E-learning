package io.train.modules.business.course.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.modules.business.course.entity.CourseRecord;
import io.train.modules.business.course.service.CourseRecourdService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.train.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:39:34
 */
@RestController
@RequestMapping("generator/courserecord")
public class CourseRecordController {
    @Autowired
    private CourseRecourdService courseRecourdService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:course:course:list")
    public R list(@RequestParam Map<String, Object> params){
        List<CourseRecord> page = courseRecourdService.queryList(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:course:course:info")
    public R info(@PathVariable("uuid") String uuid){
    	CourseRecord course = courseRecourdService.getById(uuid);
        return R.ok().put("course", course);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:course:course:save")
    public R save(@RequestBody CourseRecord course){
    	courseRecourdService.save(course);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:course:course:update")
    public R update(@RequestBody CourseRecord course){
    	courseRecourdService.updateById(course);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:course:course:delete")
    public R delete(@RequestBody String[] uuids){
    	courseRecourdService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }

}
