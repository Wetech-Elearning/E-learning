package io.train.modules.business.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.course.entity.CourseEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:39:34
 */
public interface CourseService extends IService<CourseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CourseEntity> listAll(Map<String, Object> params);

    PageUtils queryPageByStudent(Map<String, Object> params);

    public List<CourseEntity> getCourseEntityByClassId(String classId);

    public List<CourseEntity> getCourseEntityByCourseType(String courseType);
    
    /**
     * 根据课程包类型和状态查询课程
     * @author: liyajie 
     * @date: 2022/5/6 9:39
     * @param courseType
     * @return java.util.List<io.train.modules.business.common.course.entity.CourseEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    public List<CourseEntity> getCourseEntityByCourseTypeAndStatus(String courseType, String Status);

    public List<CourseEntity> getCourseEntityByCourseType(String courseType,String studentId);

    /**
     * 根据课程包ID和课程类型查询
     * @author: liyajie
     * @date: 2021/9/14 1:21
     * @param courseType
     * @param courseType
     * @return java.util.List<io.train.modules.business.common.course.entity.CourseEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseEntity> getCourseEntityByCourseTypeIdAndCourseType(String relatedCourseTypeId,String courseType);

    public CourseEntity getPageByStudentCourseId(String courseId,String studentId);
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
     * 发布课程
     * @author: liyajie
     * @date: 2021/10/22 1:13
     * @param uuid
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void release(String uuid);

    /**
     * @description 根据证书id查询课程
     * @param certificateIds
     * @return java.util.List<io.train.modules.business.common.course.entity.CourseEntity>
     * @author liyajie
     * @createTime 2021/11/24 1:15
     **/
    List<CourseEntity> getByCertificateIds(String[] certificateIds);
}
