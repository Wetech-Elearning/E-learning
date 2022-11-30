package io.train.modules.business.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.question.entity.MultiQuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * 选择题题库表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-28 00:05:00
 */
public interface MultiQuestionService extends IService<MultiQuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据试卷id查询题目
     * @author: liyajie
     * @date: 2021/9/8 2:16
     * @param questionId
     * @return java.util.List<io.train.modules.business.common.question.entity.MultiQuestionEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<MultiQuestionEntity> getListByQuestionId(String questionId);

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
    List<MultiQuestionEntity> getAllQuestionsBySubject(String subject);

    /**
     * 根据课程id和类型查询题目
     * @author: liyajie
     * @date: 2021/9/8 2:17
     * @param subject
     * @param questionType
     * @return java.util.List<io.train.modules.business.common.question.entity.MultiQuestionEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<MultiQuestionEntity> getAllQuestionsBySubjectAndType(String subject, String questionType);

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
}

