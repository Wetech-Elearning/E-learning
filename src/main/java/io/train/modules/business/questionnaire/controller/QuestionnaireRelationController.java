package io.train.modules.business.questionnaire.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.questionnaire.entity.QuestionnaireRelationEntity;
import io.train.modules.business.questionnaire.service.QuestionnaireRelationService;
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
 * @date 2021-10-25 00:14:56
 */
@RestController
@RequestMapping("generator/questionnairerelation")
public class QuestionnaireRelationController {
    @Autowired
    private QuestionnaireRelationService questionnaireRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("business:questionnairerelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = questionnaireRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 计数
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params){
        String userId = String.valueOf(params.get("userId"));
        Integer count = questionnaireRelationService.countByUserId(userId);
        return R.ok().put("data", count);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:questionnairerelation:info")
    public R info(@PathVariable("uuid") Long uuid){
		QuestionnaireRelationEntity questionnaireRelation = questionnaireRelationService.getById(uuid);

        return R.ok().put("questionnaireRelation", questionnaireRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:questionnairerelation:save")
    public R save(@RequestBody QuestionnaireRelationEntity questionnaireRelation){
		questionnaireRelationService.save(questionnaireRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:questionnairerelation:update")
    public R update(@RequestBody QuestionnaireRelationEntity questionnaireRelation){
		questionnaireRelationService.updateById(questionnaireRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:questionnairerelation:delete")
    public R delete(@RequestBody Long[] uuids){
		questionnaireRelationService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
