package io.train.modules.business.question.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.modules.business.paperquestion.entity.PaperQuestionEntity;
import io.train.modules.business.paperquestion.service.PaperQuestionService;
import io.train.modules.business.question.entity.MultiQuestionEntity;
import io.train.modules.business.question.service.MultiQuestionService;
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
 * 选择题题库表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-28 00:05:00
 */
@RestController
@RequestMapping("generator/multiquestion")
public class MultiQuestionController {
    @Autowired
    private MultiQuestionService multiQuestionService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:multiquestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = multiQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:multiquestion:info")
    public R info(@PathVariable("uuid") String  uuid){
		MultiQuestionEntity multiQuestion = multiQuestionService.getById(uuid);

        return R.ok().put("multiQuestion", multiQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:multiquestion:add")
    public R save(@RequestBody MultiQuestionEntity multiQuestion){
		multiQuestionService.save(multiQuestion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:multiquestion:update")
    public R update(@RequestBody MultiQuestionEntity multiQuestion){
		multiQuestionService.updateById(multiQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:multiquestion:delete")
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
            multiQuestionService.removeByIds(Arrays.asList(questionids));
        }else {
            return R.error("该试题已被添加到试卷无法删除");
        }

        return R.ok();
    }

}
