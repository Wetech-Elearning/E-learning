package io.train.modules.business.record.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.record.entity.StudentStudyRecordEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:43:20
 */
public interface StudentStudyRecordService extends IService<StudentStudyRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    public void saveOrupdateRecoard(StudentStudyRecordEntity entity);
    
    public void asyncStudyRecord(StudentStudyRecordEntity studentStudyRecord);

    /**
     * 根据用户id和课程id查询学习记录
     * @author: liyajie
     * @date: 2021/9/26 22:12
     * @param userId
     * @param courseId
     * @return io.train.modules.business.common.record.entity.StudentStudyRecordEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    StudentStudyRecordEntity getRecordByUserIdAndCourseId(String userId,Long courseId);

}

