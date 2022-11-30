package io.train.modules.business.coursereport.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.coursereport.entity.CourseReportRecordEntity;
import io.train.modules.business.coursereport.service.CourseReportRecordService;
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
@RequestMapping("generator/coursereportrecord")
public class CourseReportRecordController {
    @Autowired
    private CourseReportRecordService courseReportRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:coursereportrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = courseReportRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:coursereportrecord:info")
    public R info(@PathVariable("uuid") Long uuid){
		CourseReportRecordEntity courseReportRecord = courseReportRecordService.getById(uuid);

        return R.ok().put("courseReportRecord", courseReportRecord);
    }

    /**
     * 信息
     */
    @GetMapping("/getByCourseReportIdAndStudentId")
    public R getByCourseReportIdAndStudentId(@RequestParam Map<String, Object> params){
        String courseReportId = String.valueOf(params.get("courseReportId"));
        String studentId = String.valueOf(params.get("studentId"));
        CourseReportRecordEntity courseReportRecord = courseReportRecordService.getByCourseReportIdAndStudentId(courseReportId,studentId);
        return R.ok().put("courseReportRecord", courseReportRecord);
    }

    /**
     * 信息
     */
    @GetMapping("/getSubmitNums")
    public R getSubmitNums(@RequestParam Map<String, Object> params){
        String courseReportId = String.valueOf(params.get("courseReportId"));
        String studentId = String.valueOf(params.get("studentId"));
        Integer submitNums = courseReportRecordService.getSubmitNums(courseReportId,studentId);
        return R.ok().put("submitNums", submitNums);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("business:coursereportrecord:add")
    public R save(@RequestBody CourseReportRecordEntity courseReportRecord){
		courseReportRecordService.save(courseReportRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:coursereportrecord:update")
    public R update(@RequestBody CourseReportRecordEntity courseReportRecord){
		courseReportRecordService.updateById(courseReportRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:coursereportrecord:delete")
    public R delete(@RequestBody Long[] uuids){
		courseReportRecordService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

    /**
     * 评分
     */
    @RequestMapping("/markScore")
    public R markScore(@RequestParam Map<String, Object> params){
        String courseReportId = String.valueOf(params.get("courseReportId"));
        String score = String.valueOf(params.get("score"));
        String comment = String.valueOf(params.get("comment"));
        courseReportRecordService.markScore(courseReportId, score, comment);
        return R.ok();
    }

}
