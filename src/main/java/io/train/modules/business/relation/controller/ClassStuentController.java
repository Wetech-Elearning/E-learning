package io.train.modules.business.relation.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.modules.business.relation.entity.ClassStudentEntity;
import io.train.modules.business.relation.service.ClassStuentService;
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
@RequestMapping("generator/classstuent")
public class ClassStuentController {
    @Autowired
    private ClassStuentService classStuentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:classstuent:classstuent:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = classStuentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{classId}")
    @RequiresPermissions("business:classstuent:classstuent:info")
    public R info(@PathVariable("classId") String classId){
		ClassStudentEntity classStuent = classStuentService.getById(classId);

        return R.ok().put("classStuent", classStuent);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:classstuent:classstuent:save")
    public R save(@RequestBody ClassStudentEntity classStuent){
		classStuentService.save(classStuent);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:classstuent:classstuent:update")
    public R update(@RequestBody ClassStudentEntity classStuent){
		classStuentService.updateById(classStuent);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:classstuent:classstuent:delete")
    public R delete(@RequestBody String[] classIds){
		classStuentService.removeByIds(Arrays.asList(classIds));

        return R.ok();
    }

}
