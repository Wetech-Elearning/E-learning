package io.train.modules.business.record.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import io.train.modules.business.record.entity.StudentStudyRecordEntity;
import io.train.modules.business.record.service.StudentStudyRecordService;
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
 * @date 2021-07-05 10:43:20
 */
@RestController
@RequestMapping("generator/studentstudyrecord")
public class StudentStudyRecordController {
    @Autowired
    private StudentStudyRecordService studentStudyRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = studentStudyRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    public R info(@PathVariable("userId") String userId){
		StudentStudyRecordEntity studentStudyRecord = studentStudyRecordService.getById(userId);
        return R.ok().put("studentStudyRecord", studentStudyRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StudentStudyRecordEntity studentStudyRecord){
    	studentStudyRecord.setDateTime(new Date());
		studentStudyRecordService.saveOrupdateRecoard(studentStudyRecord);
		studentStudyRecord.setOnlineTime("0");
		studentStudyRecordService.asyncStudyRecord(studentStudyRecord);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody StudentStudyRecordEntity studentStudyRecord){
		studentStudyRecordService.updateById(studentStudyRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:studentstudyrecord:studentstudyrecord:delete")
    public R delete(@RequestBody String[] userIds){
		studentStudyRecordService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }

}
