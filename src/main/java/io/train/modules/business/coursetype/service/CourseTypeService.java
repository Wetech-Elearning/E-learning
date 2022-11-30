package io.train.modules.business.coursetype.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:40:30
 */
public interface CourseTypeService extends IService<CourseTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取课程列表
     * @author: liyajie 
     * @date: 2022/6/20 18:13
     * @param paramMap
     * @return java.util.List<io.train.modules.business.common.coursetype.entity.CourseTypeEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseTypeEntity> getCourseTypeList(Map<String,Object> paramMap);

    List<CourseTypeEntity> getAllCouseType(Map<String, Object> params);

    /**
     * 查询一级课程包
     * @author: liyajie
     * @date: 2021/10/28 22:42
     * @param params
     * @return java.util.List<io.train.modules.business.common.coursetype.entity.CourseTypeEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseTypeEntity> listOneLevelCourseType(Map<String, Object> params);

    /**
     * 查询二级课程包
     * @author: liyajie
     * @date: 2021/9/1 22:59
     * @param params
     * @return java.util.List<io.train.modules.business.common.coursetype.entity.CourseTypeEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseTypeEntity> listTwoLevelCourseType(Map<String, Object> params);

    List<CourseTypeEntity> getAllCouseTypeByStudent(String studentId);

    CourseTypeEntity getAllCouseTypeByStudent(String couseTypeId,String studentId);

    /**
     * 首页课程展示
     * @author: liyajie
     * @date: 2021/8/28 9:53
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    Map<String,Object> showHome();

    /**
     * 根据父级课程包id查询二级课程包
     * @author: liyajie
     * @date: 2021/8/25 23:33
     * @param parentCouseType
     * @return java.util.List<io.train.modules.business.common.coursetype.entity.CourseTypeEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseTypeEntity> getByParentCouseType(String parentCouseType);

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

    List<CourseTypeEntity> getByParentCouseTypeByIds(String[] ids);

    /**
     * 根据证书id查询二级课程包
     * @author: liyajie
     * @date: 2021/8/25 23:33
     * @param certificateIds
     * @return java.util.List<io.train.modules.business.common.coursetype.entity.CourseTypeEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseTypeEntity> getTwoLevelByCertificateId(String[] certificateIds);
}

