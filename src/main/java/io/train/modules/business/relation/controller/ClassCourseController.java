package io.train.modules.business.relation.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.modules.business.relation.entity.ClassCourseEntity;
import io.train.modules.business.relation.service.ClassCourseService;
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
 * @date 2021-07-05 10:48:33
 */
@RestController
@RequestMapping("generator/classcourse")
public class ClassCourseController {
    @Autowired
    private ClassCourseService classCourseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:classcourse:classcourse:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = classCourseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{classId}")
    @RequiresPermissions("business:classcourse:classcourse:info")
    public R info(@PathVariable("classId") String classId){
		ClassCourseEntity classCourse = classCourseService.getById(classId);

        return R.ok().put("classCourse", classCourse);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:classcourse:classcourse:save")
    public R save(@RequestBody ClassCourseEntity classCourse){
		classCourseService.save(classCourse);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:classcourse:classcourse:update")
    public R update(@RequestBody ClassCourseEntity classCourse){
		classCourseService.updateById(classCourse);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:classcourse:classcourse:delete")
    public R delete(@RequestBody String[] classIds){
		classCourseService.removeByIds(Arrays.asList(classIds));

        return R.ok();
    }

}
