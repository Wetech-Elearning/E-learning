package io.train.modules.business.questionnaire.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.questionnaire.entity.QuestionnaireRelationEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-10-25 00:14:56
 */
public interface QuestionnaireRelationService extends IService<QuestionnaireRelationEntity> {

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
    void changeStatusByIds(List<String> uuids,String userId);

    /**
     * 根据用户名称查询问卷数量
     * @author: liyajie
     * @date: 2021/10/25 3:54
     * @param userId
     * @return java.lang.Integer
     * @exception:
     * @update:
     * @updatePerson:
     **/
    Integer countByUserId(String userId);

    /**
     * 根据用户名称查询问卷
     * @author: liyajie
     * @date: 2022/3/14 11:24
     * @param userId-用户id
     * @param status-问卷状态:0:未答;1:已答
     * @return java.util.List<java.lang.String>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<String> getByUserId(String userId, String status);
}

