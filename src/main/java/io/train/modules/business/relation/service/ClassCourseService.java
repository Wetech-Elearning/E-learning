package io.train.modules.business.relation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.relation.entity.ClassCourseEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:48:33
 */
public interface ClassCourseService extends IService<ClassCourseEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据班级id查询班级关联的课程
     * @author: liyajie 
     * @date: 2022/6/30 14:50
     * @param classId
     * @return java.util.List<io.train.modules.business.common.relation.entity.ClassCourseEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<ClassCourseEntity> getClassCourseByClassId(Long classId);

    /**
     * 根据二级课程包id查询班级关联的课程
     * @author: liyajie 
     * @date: 2022/8/17 17:27
     * @param courseTypeId
     * @return java.util.List<io.train.modules.business.common.relation.entity.ClassCourseEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<ClassCourseEntity> getClassCourseByCourseTypeId(Long courseTypeId);
}

