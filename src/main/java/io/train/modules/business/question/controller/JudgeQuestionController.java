package io.train.modules.business.question.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.modules.business.paperquestion.entity.PaperQuestionEntity;
import io.train.modules.business.paperquestion.service.PaperQuestionService;
import io.train.modules.business.question.entity.JudgeQuestionEntity;
import io.train.modules.business.question.service.JudgeQuestionService;
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
 * 判断题题库表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-28 00:05:00
 */
@RestController
@RequestMapping("generator/judgequestion")
public class JudgeQuestionController {
    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:judgequestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = judgeQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:judgequestion:info")
    public R info(@PathVariable("uuid") String uuid){
		JudgeQuestionEntity judgeQuestion = judgeQuestionService.getById(uuid);

        return R.ok().put("judgeQuestion", judgeQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:judgequestion:add")
    public R save(@RequestBody JudgeQuestionEntity judgeQuestion){
		judgeQuestionService.save(judgeQuestion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:judgequestion:update")
    public R update(@RequestBody JudgeQuestionEntity judgeQuestion){
		judgeQuestionService.updateById(judgeQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:judgequestion:delete")
    public R delete(@RequestBody String[] questionids){
        boolean deleteFlag = true;
        for (String questionid : questionids) {
            PaperQuestionEntity paperQuestionEntity = paperQuestionService.getByQuestionId(questionid);
            if(null != paperQuestionEntity){
                deleteFlag =false;
                break;
            }
        }
        if(deleteFlag){
            judgeQuestionService.removeByIds(Arrays.asList(questionids));
        }else {
            return R.error("该试题已被添加到试卷无法删除");
        }

        return R.ok();
    }

}
