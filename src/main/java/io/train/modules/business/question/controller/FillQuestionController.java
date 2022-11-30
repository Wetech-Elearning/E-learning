package io.train.modules.business.question.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.modules.business.paperquestion.entity.PaperQuestionEntity;
import io.train.modules.business.paperquestion.service.PaperQuestionService;
import io.train.modules.business.question.entity.FillQuestionEntity;
import io.train.modules.business.question.service.FillQuestionService;
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
 * 填空题题库
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-28 00:05:00
 */
@RestController
@RequestMapping("generator/fillquestion")
public class FillQuestionController {
    @Autowired
    private FillQuestionService fillQuestionService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:fillquestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = fillQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:fillquestion:info")
    public R info(@PathVariable("uuid") String  uuid){
		FillQuestionEntity fillQuestion = fillQuestionService.getById(uuid);
        return R.ok().put("fillQuestion", fillQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:fillquestion:add")
    public R save(@RequestBody FillQuestionEntity fillQuestion){
		fillQuestionService.save(fillQuestion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:fillquestion:update")
    public R update(@RequestBody FillQuestionEntity fillQuestion){
		fillQuestionService.updateById(fillQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:fillquestion:delete")
    public R delete(@RequestBody String[] questionids){
        boolean deleteFlag = true;
        for (String uuid : questionids) {
            PaperQuestionEntity paperQuestionEntity = paperQuestionService.getByQuestionId(uuid);
            if(null != paperQuestionEntity){
                deleteFlag =false;
                break;
            }
        }
        if(deleteFlag){
            fillQuestionService.removeByIds(Arrays.asList(questionids));
        }else {
            return R.error("该试题已被添加到试卷无法删除");
        }

        return R.ok();
    }

}
