package io.train.modules.business.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.paper.entity.ExamPaperInfosEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:51:50
 */
public interface ExamPaperInfosService extends IService<ExamPaperInfosEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<ExamPaperInfosEntity> queryAll(Map<String, Object> params);
    
    List<ExamPaperInfosEntity> listByUser(String userId,String examPaperName,int start,int limit);
    int listByUserCount(String userId,String examPaperName);

    List<ExamPaperInfosEntity> listByClassId(String classId);

    /**
     * 删除试卷
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
     * 更新试卷状态
     * @author: liyajie
     * @date: 2021/8/29 23:48
     * @param uuid
     * @param status
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void updateStatus(String uuid,String status);

    /**
     * 根据课程查询课程关联的试卷
     * @author: liyajie
     * @date: 2021/9/4 0:37
     * @param subject
     * @return io.train.modules.business.common.paper.entity.ExamPaperInfosEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<ExamPaperInfosEntity> getExamPaperInfosEntityBySubject(String subject);

    /**
     * 初始化试卷下拉框
     * @author: liyajie
     * @date: 2021/9/4 18:59
     * @param
     * @return java.util.List<io.train.modules.business.common.paper.entity.ExamPaperInfosEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<ExamPaperInfosEntity> initPaperOptions();
}

