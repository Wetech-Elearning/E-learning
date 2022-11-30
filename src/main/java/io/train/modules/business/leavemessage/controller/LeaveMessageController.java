package io.train.modules.business.leavemessage.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.train.modules.business.leavemessage.entity.LeaveMessageEntity;
import io.train.modules.business.leavemessage.service.LeaveMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:41:24
 */
@RestController
@RequestMapping("generator/leavemessage")
public class LeaveMessageController {
    @Autowired
    private LeaveMessageService leaveMessageService;
    

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = leaveMessageService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/listGroupByCourseTypeId")
    public R listGroupByCourseTypeId(@RequestParam Map<String, Object> params){
        List<LeaveMessageEntity> leaveMessageEntityList = leaveMessageService.listGroupByCourseTypeId(params);
        return R.ok().put("data", leaveMessageEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    public R info(@PathVariable("uuid") String uuid){
    	LeaveMessageEntity lecturer = leaveMessageService.getById(uuid);

        return R.ok().put("data", lecturer);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody LeaveMessageEntity lecturer){
    	lecturer.setCreateTime(new Date());
    	leaveMessageService.save(lecturer);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody LeaveMessageEntity leaveMessageEntity){
    	leaveMessageService.updateById(leaveMessageEntity);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("business:lecturer:lecturer:delete")
    public R delete(@RequestBody String[] uuids){
    	leaveMessageService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }

    /**
     * 回复评论
     * @author: liyajie 
     * @date: 2022/6/20 16:26
     * @param leaveMessageEntity
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @PostMapping("/reply")
    public R reply(@RequestBody LeaveMessageEntity leaveMessageEntity){
        leaveMessageService.reply(leaveMessageEntity);

        return R.ok();
    }
}
