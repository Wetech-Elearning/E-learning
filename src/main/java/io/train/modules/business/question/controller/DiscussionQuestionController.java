package io.train.modules.business.question.controller;

import java.util.Arrays;
import java.util.Map;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.modules.business.paperquestion.entity.PaperQuestionEntity;
import io.train.modules.business.paperquestion.service.PaperQuestionService;
import io.train.modules.business.question.entity.DiscussionQuestionEntity;
import io.train.modules.business.question.service.DiscussionQuestionService;
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
 * @date 2021-09-12 16:20:26
 */
@RestController
@RequestMapping("generator/discussionquestion")
public class DiscussionQuestionController {
    @Autowired
    private DiscussionQuestionService discussionQuestionService;

    @Autowired
    private PaperQuestionService paperQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:discussionquestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = discussionQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:discussionquestion:info")
    public R info(@PathVariable("uuid") Long uuid){
		DiscussionQuestionEntity discussionQuestion = discussionQuestionService.getById(uuid);

        return R.ok().put("discussionQuestion", discussionQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:discussionquestion:add")
    public R save(@RequestBody DiscussionQuestionEntity discussionQuestion){
		discussionQuestionService.save(discussionQuestion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:discussionquestion:update")
    public R update(@RequestBody DiscussionQuestionEntity discussionQuestion){
		discussionQuestionService.updateById(discussionQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:discussionquestion:delete")
    public R delete(@RequestBody String[] uuids){
        boolean deleteFlag = true;
        for (String uuid : uuids) {
            PaperQuestionEntity paperQuestionEntity = paperQuestionService.getByQuestionId(uuid);
            if(null != paperQuestionEntity){
                deleteFlag =false;
                break;
            }
        }
        if(deleteFlag){
            discussionQuestionService.removeByIds(Arrays.asList(uuids));
        }else {
            return R.error("该试题已被添加到试卷无法删除");
        }
        return R.ok();
    }

}
