package io.train.modules.business.relation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.relation.entity.UserExamInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:55:17
 */
public interface UserExamInfoService extends IService<UserExamInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询最大考试次数 
     * @author: liyajie 
     * @date: 2022/7/5 18:20
     * @param uuid
     * @param userId
     * @return java.lang.Integer
     * @exception:
     * @update:
     * @updatePerson:
     **/
    Integer getMaxNum(String uuid, String userId);

    /**
     * 保存用户考试信息
     * @author: liyajie 
     * @date: 2022/7/5 18:20
     * @param userExamInfo
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void saveUserExamInfo(UserExamInfoEntity userExamInfo);
    
    /**
     * 根据试卷id和用户id查询最新的考试次数
     * @author: liyajie 
     * @date: 2022/6/13 18:22
     * @param paperId
     * @param userId
     * @return java.lang.Integer
     * @exception:
     * @update:
     * @updatePerson:
     **/
    Integer getMaxExamNumByPaperIdAndUserId(String paperId, String userId);
}

