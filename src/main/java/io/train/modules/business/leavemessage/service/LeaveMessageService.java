package io.train.modules.business.leavemessage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.leavemessage.entity.LeaveMessageEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:41:24
 */
public interface LeaveMessageService extends IService<LeaveMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据课程包分组查询评论
     * @author: liyajie 
     * @date: 2022/7/24 14:22
     * @param params
     * @return java.util.List<io.train.modules.business.common.leavemessage.entity.LeaveMessageEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<LeaveMessageEntity> listGroupByCourseTypeId(Map<String, Object> params);

    /**
     * 删除评论
     * @author: liyajie 
     * @date: 2022/6/20 16:21
     * @param uuids
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void removeByIds(List<String> uuids);
    
    void reply(LeaveMessageEntity leaveMessageEntity);
}

