package io.train.modules.business.questionpark.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import io.train.modules.business.questionpark.entity.QuestionParkContent;
import io.train.modules.business.questionpark.service.QuestionParkService;

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
 * @date 2021-07-05 10:41:24
 */
@RestController
@RequestMapping("generator/questionpark")
public class QuestionParkController {
	
	
    @Autowired
    private QuestionParkService questionParkService;
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = questionParkService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:lecturer:lecturer:info")
    public R info(@PathVariable("uuid") String uuid){
    	QuestionParkContent lecturer = questionParkService.getById(uuid);
        return R.ok().put("lecturer", lecturer);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:lecturer:lecturer:save")
    public R save(@RequestBody QuestionParkContent lecturer){
    	lecturer.setCreateTime(new Date());
    	questionParkService.save(lecturer);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:lecturer:lecturer:update")
    public R update(@RequestBody QuestionParkContent lecturer){
    	questionParkService.updateById(lecturer);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:lecturer:lecturer:delete")
    public R delete(@RequestBody String[] uuids){
    	questionParkService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }
    
    

}
