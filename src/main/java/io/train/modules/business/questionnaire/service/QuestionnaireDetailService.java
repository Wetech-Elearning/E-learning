package io.train.modules.business.questionnaire.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.questionnaire.entity.QuestionnaireDetailEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-10-24 04:53:55
 */
public interface QuestionnaireDetailService extends IService<QuestionnaireDetailEntity> {

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
     * 更新状态
     * @author: liyajie
     * @date: 2021/8/30 0:14
     * @param uuids
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void changeStatusByIds(List<String> uuids);

    /**
     * 根据问卷id，查询问卷包含的题目
     * @author: liyajie
     * @date: 2021/10/25 2:02
     * @param uuid
     * @return java.util.List<io.train.modules.business.common.questionnaire.entity.QuestionnaireDetailEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<QuestionnaireDetailEntity> getQuestionnaireById(String uuid);

    /**
     * 根据试题id删除
     * @author: liyajie
     * @date: 2021/9/12 22:20
     * @param questionIds
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void deleteByQuestionIds(List<String> questionIds, String questionnaireId);

    /**
     * 根据问卷id和问卷题目id查询问卷
     * @author: liyajie
     * @date: 2022/3/6 5:14
     * @param questionnaireId
     * @param questionId
     * @return io.train.modules.business.common.questionnaire.entity.QuestionnaireDetailEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    QuestionnaireDetailEntity getByQuestionIdAndQuestionnaireId(String questionnaireId, String questionId);
}

