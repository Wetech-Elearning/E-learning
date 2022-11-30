package io.train.modules.business.questionnaire.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.questionnaire.entity.QuestionnaireDetailEntity;
import io.train.modules.business.questionnaire.entity.QuestionnaireQuestionEntity;
import io.train.modules.business.questionnaire.service.QuestionnaireDetailService;
import io.train.modules.business.questionnaire.service.QuestionnaireQuestionService;
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
 * @date 2021-10-24 04:53:55
 */
@RestController
@RequestMapping("generator/questionnairequestion")
public class QuestionnaireQuestionController {
    @Autowired
    private QuestionnaireQuestionService questionnaireQuestionService;

    @Autowired
    private QuestionnaireDetailService questionnaireDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:questionnairequestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = questionnaireQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询所有的问卷题目
     */
    @RequestMapping("/listAll")
    public R listAll(@RequestParam Map<String, Object> params){
        String questionnaireId = String.valueOf(params.get("questionnaireId"));
        // 查询所有问卷调查题目
        List<QuestionnaireQuestionEntity> questionnaireQuestionEntityList = questionnaireQuestionService.listAll();
        if(null != questionnaireQuestionEntityList && questionnaireQuestionEntityList.size() > 0){
            for (QuestionnaireQuestionEntity questionnaireQuestionEntity : questionnaireQuestionEntityList) {
                String questionType = questionnaireQuestionEntity.getQuestionType();
                if("radio".equals(questionType)){
                    questionnaireQuestionEntity.setQuestion("单项选择:" + questionnaireQuestionEntity.getQuestion());
                }
                if("multi".equals(questionType)){
                    questionnaireQuestionEntity.setQuestion("多项选择:" +  questionnaireQuestionEntity.getQuestion());
                }
                if("discussion".equals(questionType)){
                    questionnaireQuestionEntity.setQuestion("简答:" +  questionnaireQuestionEntity.getQuestion());
                }
                if("article".equals(questionType)){
                    questionnaireQuestionEntity.setQuestion("长文:" +  questionnaireQuestionEntity.getQuestion());
                }
            }
        }
        // 查询当前调查问卷已有的题目
        List<QuestionnaireDetailEntity> questionnaireDetailEntityList = questionnaireDetailService.getQuestionnaireById(questionnaireId);
 
        return R.ok().put("data",questionnaireQuestionEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:questionnairequestion:info")
    public R info(@PathVariable("uuid") Long uuid){
		QuestionnaireQuestionEntity questionnaireQuestion = questionnaireQuestionService.getById(uuid);

        return R.ok().put("questionnaireQuestion", questionnaireQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:questionnairequestion:save")
    public R save(@RequestBody QuestionnaireQuestionEntity questionnaireQuestion){
		questionnaireQuestionService.save(questionnaireQuestion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:questionnairequestion:update")
    public R update(@RequestBody QuestionnaireQuestionEntity questionnaireQuestion){
		questionnaireQuestionService.updateById(questionnaireQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:questionnairequestion:delete")
    public R delete(@RequestBody Long[] uuids){
		questionnaireQuestionService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
