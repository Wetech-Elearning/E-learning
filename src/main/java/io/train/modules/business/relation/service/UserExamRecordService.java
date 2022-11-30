package io.train.modules.business.relation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.relation.entity.UserExamRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-09-12 16:20:26
 */
public interface UserExamRecordService extends IService<UserExamRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询学员当前考试题的作答
     * @author: liyajie 
     * @date: 2022/6/10 14:54
     * @param paperId - 试卷id
     * @param questionId - 试题id
     * @param userId - 用户id
     * @param examNum - 当前考试次数
     * @return io.train.modules.business.common.relation.entity.UserExamRecordEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    UserExamRecordEntity getCurrentUserAnswers(String paperId,String questionId,String userId,Integer examNum);

    /**
     * 查询学员当前试卷的作答
     * @author: liyajie
     * @date: 2022/6/10 14:54
     * @param paperId - 试卷id
     * @param userId - 用户id
     * @param examNum - 当前考试次数
     * @return io.train.modules.business.common.relation.entity.UserExamRecordEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<UserExamRecordEntity> getCurrentUserPapteAnswers(String paperId, String userId, Integer examNum);
}

