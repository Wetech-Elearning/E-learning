package io.train.modules.business.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.question.entity.TranslateQuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-09-12 18:37:56
 */
public interface TranslateQuestionService extends IService<TranslateQuestionEntity> {

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
     * 根据课程id查询题目
     * @author: liyajie
     * @date: 2021/9/8 2:17
     * @param subject
     * @return java.util.List<io.train.modules.business.common.question.entity.MultiQuestionEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<TranslateQuestionEntity> getAllQuestionsBySubject(String subject);
}