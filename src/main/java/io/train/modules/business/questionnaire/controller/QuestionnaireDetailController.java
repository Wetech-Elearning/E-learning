package io.train.modules.business.questionnaire.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.questionnaire.dto.QuestionDto;
import io.train.modules.business.questionnaire.dto.QuestionnaireDto;
import io.train.modules.business.questionnaire.entity.QuestionnaireDetailEntity;
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
@RequestMapping("generator/questionnairedetail")
public class QuestionnaireDetailController {
    @Autowired
    private QuestionnaireDetailService questionnaireDetailService;

    @Autowired
    private QuestionnaireQuestionService questionnaireQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:questionnairedetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = questionnaireDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    //@RequiresPermissions("business:questionnairedetail:info")
    public R info(@PathVariable("uuid") String uuid){
		List<QuestionnaireDetailEntity> questionnaireDetailEntityList = questionnaireDetailService.getQuestionnaireById(uuid);
        List<String> questionIds = questionnaireDetailEntityList.stream().map(QuestionnaireDetailEntity::getQuestionId).collect(Collectors.toList());
		return R.ok().put("data", questionnaireQuestionService.getByQuestionIds(questionIds));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:questionnairedetail:save")
    public R save(@RequestBody QuestionnaireDto questionnaireDto){
        for (QuestionDto questionDto:questionnaireDto.getQuestionIds()) {
            QuestionnaireDetailEntity questionnaireDetailEntity = questionnaireDetailService.getByQuestionIdAndQuestionnaireId(questionnaireDto.getQuestionnaireId(), questionDto.getKey());
            if(null == questionnaireDetailEntity){
                QuestionnaireDetailEntity questionnaireDetail = new QuestionnaireDetailEntity();
                questionnaireDetail.setQuestionnaireId(questionnaireDto.getQuestionnaireId());
                questionnaireDetail.setQuestionId(questionDto.getKey());
                questionnaireDetail.setCreater(questionnaireDto.getCreater());
                questionnaireDetail.setCreateDate(questionnaireDto.getCreateDate());
                questionnaireDetail.setDeleteFlag("0");
                questionnaireDetailService.save(questionnaireDetail);
            }
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:questionnairedetail:update")
    public R update(@RequestBody QuestionnaireDetailEntity questionnaireDetail){
		questionnaireDetailService.updateById(questionnaireDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:questionnairedetail:delete")
    public R delete(@RequestBody Long[] uuids){
		questionnaireDetailService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
