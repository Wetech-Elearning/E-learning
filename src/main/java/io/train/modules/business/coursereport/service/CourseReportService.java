package io.train.modules.business.coursereport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.coursereport.entity.CourseReportEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-08-09 14:37:08
 */
public interface CourseReportService extends IService<CourseReportEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 发布
     * @author: liyajie 
     * @date: 2022/8/9 17:11
     * @param uuid
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void release(Long uuid, Long tenantId);
    
    /**
     * 查询学生的课程报告
     * @author: liyajie 
     * @date: 2022/8/18 8:30
     * @param 
     * @return java.util.List<io.train.modules.business.common.coursereport.entity.CourseReportEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseReportEntity> listCourseReport(String student, String courseTypeId);
}

