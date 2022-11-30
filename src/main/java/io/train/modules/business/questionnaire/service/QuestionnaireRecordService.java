package io.train.modules.business.questionnaire.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.questionnaire.entity.QuestionnaireRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-10-25 00:14:56
 */
public interface QuestionnaireRecordService extends IService<QuestionnaireRecordEntity> {

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
}

