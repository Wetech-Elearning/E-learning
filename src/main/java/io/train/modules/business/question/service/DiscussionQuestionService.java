package io.train.modules.business.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.question.entity.DiscussionQuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-09-12 16:20:26
 */
public interface DiscussionQuestionService extends IService<DiscussionQuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除
     * @author: liyajie
     * @date: 2021/8/30 0:14
     * @param uuids
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void removeByIds(List<String> uuids);


    /**
     * 根据课程查询试题
     * @author: liyajie
     * @date: 2021/9/12 17:54
     * @param subject
     * @return java.util.List<io.train.modules.business.common.question.entity.DiscussionQuestionEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<DiscussionQuestionEntity> getAllQuestionsBySubject(String subject);
}

