package io.train.modules.business.questionnaire.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.questionnaire.entity.QuestionnaireEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-10-24 04:53:55
 */
public interface QuestionnaireService extends IService<QuestionnaireEntity> {

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
     * 更新发布状态
     * @author: liyajie
     * @date: 2022/3/6 5:30
     * @param status
     * @param uuid
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void updateStatus(int status, String uuid);
}

