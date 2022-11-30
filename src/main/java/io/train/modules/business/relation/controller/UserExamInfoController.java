package io.train.modules.business.relation.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.modules.business.paper.entity.ExamPaperInfosEntity;
import io.train.modules.business.paper.service.ExamPaperInfosService;
import io.train.modules.business.relation.entity.UserExamInfoEntity;
import io.train.modules.business.relation.service.UserExamInfoService;
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
 * @date 2021-07-05 10:55:17
 */
@RestController
@RequestMapping("generator/userexaminfo")
public class UserExamInfoController {
    @Autowired
    private UserExamInfoService userExamInfoService;

    @Autowired
    private ExamPaperInfosService examPaperInfosService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("business:userexaminfo:userexaminfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userExamInfoService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    //@RequiresPermissions("business:userexaminfo:userexaminfo:info")
    public R info(@PathVariable("uuid") String uuid){
		UserExamInfoEntity userExamInfo = userExamInfoService.getById(uuid);

        return R.ok().put("userExamInfo", userExamInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("business:userexaminfo:userexaminfo:save")
    public R save(@RequestBody UserExamInfoEntity userExamInfo){
		userExamInfoService.saveUserExamInfo(userExamInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("business:userexaminfo:userexaminfo:update")
    public R update(@RequestBody UserExamInfoEntity userExamInfo){
		userExamInfoService.updateById(userExamInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("business:userexaminfo:userexaminfo:delete")
    public R delete(@RequestBody String[] uuids){
		userExamInfoService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

    /**
     * 是否达到最大考试次数
     */
    @RequestMapping("/isMaxNum")
    public R isMaxNum(@RequestParam Map<String, Object> params){
        Boolean isMaxNum = false;
        String uuid = String.valueOf(params.get("uuid"));
        String userId = String.valueOf(params.get("userId"));
        Integer maxNum = userExamInfoService.getMaxNum(uuid,userId);
        ExamPaperInfosEntity examPaperInfosEntity = examPaperInfosService.getById(uuid);
        if(maxNum >= examPaperInfosEntity.getExamMaxNum()){
            isMaxNum = true;
        }
        return R.ok().put("data", isMaxNum);
    }
}
