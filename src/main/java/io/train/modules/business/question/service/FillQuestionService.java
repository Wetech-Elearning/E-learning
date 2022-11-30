package io.train.modules.business.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.question.entity.FillQuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * 填空题题库
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-28 00:05:00
 */
public interface FillQuestionService extends IService<FillQuestionEntity> {

    /**
     * 分页查询
     * @author: liyajie
     * @date: 2021/9/8 2:18
     * @param params
     * @return io.train.common.utils.PageUtils
     * @exception:
     * @update:
     * @updatePerson:
     **/
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据试卷id查询题目
     * @author: liyajie
     * @date: 2021/9/8 2:18
     * @param questionId
     * @return java.util.List<io.train.modules.business.common.question.entity.FillQuestionEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<FillQuestionEntity> getListByQuestionId(String questionId);

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
    List<FillQuestionEntity> getAllQuestionsBySubject(String subject);

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

