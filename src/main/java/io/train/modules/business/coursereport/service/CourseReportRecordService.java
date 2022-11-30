package io.train.modules.business.coursereport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import io.train.common.utils.PageUtils;
import io.train.modules.business.coursereport.entity.CourseReportRecordEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-08-09 14:37:08
 */
public interface CourseReportRecordService extends IService<CourseReportRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据课程报告id和学员id查询课程报告记录
     * @author: liyajie 
     * @date: 2022/8/19 0:07
     * @param courseReportId
     * @param studentId
     * @return io.train.modules.business.common.coursereport.entity.CourseReportRecordEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    CourseReportRecordEntity getByCourseReportIdAndStudentId(String courseReportId, String studentId);
    
    /**
     * 根据课程报告id和学员id查询课程报告记录提交次数
     * @author: liyajie 
     * @date: 2022/8/26 2:18
     * @param courseReportId
     * @param studentId
     * @return java.lang.Integer
     * @exception:
     * @update:
     * @updatePerson:
     **/
    Integer getSubmitNums(String courseReportId, String studentId);
    
    /**
     * 评分
     * @author: liyajie 
     * @date: 2022/9/13 15:36
     * @param courseReportId
     * @param score
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void markScore(String courseReportId, String score, String comment);
}

