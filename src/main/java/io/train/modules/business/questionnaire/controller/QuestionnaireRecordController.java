package io.train.modules.business.questionnaire.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.questionnaire.dto.QuestionnaireRecordDto;
import io.train.modules.business.questionnaire.dto.QuestionnaireRecordItem;
import io.train.modules.business.questionnaire.entity.QuestionnaireRecordEntity;
import io.train.modules.business.questionnaire.service.QuestionnaireRecordService;
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
@RequestMapping("generator/questionnairerecord")
public class QuestionnaireRecordController {
    @Autowired
    private QuestionnaireRecordService questionnaireRecordService;

    @Autowired
    private QuestionnaireRelationService questionnaireRelationService;


    private Map<String, Object> params;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:questionnairerecord:list")
    public R list(@RequestParam Map<String, Object> params){
        this.params = params;
        PageUtils page = questionnaireRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:questionnairerecord:info")
    public R info(@PathVariable("uuid") Long uuid){
		QuestionnaireRecordEntity questionnaireRecord = questionnaireRecordService.getById(uuid);

        return R.ok().put("questionnaireRecord", questionnaireRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("business:questionnairerecord:save")
    public R save(@RequestBody QuestionnaireRecordDto questionnaireRecordDto){
        for (QuestionnaireRecordItem questionnaireRecordItem:questionnaireRecordDto.getQuestionnaireRecords()) {
            QuestionnaireRecordEntity questionnaireRecordEntity = new QuestionnaireRecordEntity();
            questionnaireRecordEntity.setUserId(questionnaireRecordDto.getUserId());
            questionnaireRecordEntity.setQuestionnaireId(questionnaireRecordItem.getQuestionnaireId());
            questionnaireRecordEntity.setQuestionnaireQuestionId(questionnaireRecordItem.getQuestionnaireQuestionId());
            questionnaireRecordEntity.setQuestionnaireQuestionAnswer(questionnaireRecordItem.getQuestionnaireQuestionAnswer());
            questionnaireRecordService.save(questionnaireRecordEntity);
        }
        //更新问卷状态
        String questionnaireId =  questionnaireRecordDto.getQuestionnaireRecords().get(0).getQuestionnaireId();
        ArrayList<String> questionnaireIds = new ArrayList<String>();
        questionnaireIds.add(questionnaireId);
        questionnaireRelationService.changeStatusByIds(questionnaireIds,questionnaireRecordDto.getUserId());
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:questionnairerecord:update")
    public R update(@RequestBody QuestionnaireRecordEntity questionnaireRecord){
		questionnaireRecordService.updateById(questionnaireRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:questionnairerecord:delete")
    public R delete(@RequestBody Long[] uuids){
		questionnaireRecordService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
