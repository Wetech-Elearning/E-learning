package io.train.modules.business.questionnaire.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.questionnaire.entity.QuestionnaireQuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-10-24 04:53:55
 */
public interface QuestionnaireQuestionService extends IService<QuestionnaireQuestionEntity> {

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
     * 查询所有的问卷题目
     * @author: liyajie
     * @date: 2021/10/25 0:51
     * @param
     * @return java.util.List<io.train.modules.business.common.questionnaire.entity.QuestionnaireQuestionEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<QuestionnaireQuestionEntity> listAll();

    /**
     * 根据questionIds查询所有的问卷题目
     * @author: liyajie
     * @date: 2021/10/25 0:51
     * @param
     * @return java.util.List<io.train.modules.business.common.questionnaire.entity.QuestionnaireQuestionEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<QuestionnaireQuestionEntity> getByQuestionIds(List<String> questionIds);
}

