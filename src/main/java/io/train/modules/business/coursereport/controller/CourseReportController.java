package io.train.modules.business.coursereport.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.coursereport.entity.CourseReportEntity;
import io.train.modules.business.coursereport.service.CourseReportService;
import io.train.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-08-09 14:37:08
 */
@RestController
@RequestMapping("generator/coursereport")
public class CourseReportController extends AbstractController {
    @Autowired
    private CourseReportService courseReportService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:coursereport:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = courseReportService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:coursereport:info")
    public R info(@PathVariable("uuid") Long uuid){
		CourseReportEntity courseReport = courseReportService.getById(uuid);

        return R.ok().put("courseReport", courseReport);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:coursereport:add")
    public R save(@RequestBody CourseReportEntity courseReport){
		courseReportService.save(courseReport);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:coursereport:update")
    public R update(@RequestBody CourseReportEntity courseReport){
		courseReportService.updateById(courseReport);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:coursereport:delete")
    public R delete(@RequestBody Long[] uuids){
		courseReportService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }

    /**
     * 发布
     * @author: liyajie 
     * @date: 2022/8/9 17:10
     * @param uuid
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @GetMapping("/release/{uuid}")
    public R release(@PathVariable("uuid") Long uuid){
        courseReportService.release(uuid,getUser().getTenantId());
        return R.ok();
    }

    @RequestMapping("/listCourseReport")
    public R listCourseReport(@RequestParam Map<String, Object> params){
        String couseTypeId=""+params.get("relatedCourseTypeId");
        String studentId = ""+params.get("studentId");
        List<CourseReportEntity> courseReportEntityList = courseReportService.listCourseReport(studentId,couseTypeId);
        return R.ok().put("data", courseReportEntityList);
    }

}
