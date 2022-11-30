package io.train.modules.business.coursereport.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.coursereport.entity.CourseReportUserEntity;
import io.train.modules.business.coursereport.service.CourseReportUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-08-17 17:16:04
 */
@RestController
@RequestMapping("generator/coursereportuser")
public class CourseReportUserController {
    @Autowired
    private CourseReportUserService courseReportUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:coursereportuser:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = courseReportUserService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("generator:coursereportuser:info")
    public R info(@PathVariable("uuid") Long uuid){
		CourseReportUserEntity courseReportUser = courseReportUserService.getById(uuid);

        return R.ok().put("courseReportUser", courseReportUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:coursereportuser:save")
    public R save(@RequestBody CourseReportUserEntity courseReportUser){
		courseReportUserService.save(courseReportUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:coursereportuser:update")
    public R update(@RequestBody CourseReportUserEntity courseReportUser){
		courseReportUserService.updateById(courseReportUser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:coursereportuser:delete")
    public R delete(@RequestBody Long[] uuids){
		courseReportUserService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
