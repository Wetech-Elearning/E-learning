package io.train.modules.business.relation.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.relation.entity.UserExamRecordEntity;
import io.train.modules.business.relation.service.UserExamRecordService;
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
 * @date 2021-09-12 16:20:26
 */
@RestController
@RequestMapping("generator/userexamrecord")
public class UserExamRecordController {
    @Autowired
    private UserExamRecordService userExamRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("business:userexamrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userExamRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    //@RequiresPermissions("business:userexamrecord:info")
    public R info(@PathVariable("uuid") Long uuid){
		UserExamRecordEntity userExamRecord = userExamRecordService.getById(uuid);

        return R.ok().put("userExamRecord", userExamRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("business:userexamrecord:add")
    public R save(@RequestBody UserExamRecordEntity userExamRecord){
		userExamRecordService.save(userExamRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("business:userexamrecord:update")
    public R update(@RequestBody UserExamRecordEntity userExamRecord){
		userExamRecordService.updateById(userExamRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("business:userexamrecord:delete")
    public R delete(@RequestBody Long[] uuids){
		userExamRecordService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
