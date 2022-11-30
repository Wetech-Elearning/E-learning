package io.train.modules.business.classes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.classes.entity.ClassesEntity;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 11:24:08
 */
public interface ClassesService extends IService<ClassesEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    
    public List<ClassesEntity> queryPageByStudentId(String studentId,String className);
    
    public ClassesEntity getStudentList(ClassesEntity classEntity);
    
    public int addStudent2Class(String classUuid,String[] studentUuids);
    
    public void delStudentFromClass(String classUuid,String[] studentUuids);

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
     * 列出用户的所有课程
     * @author: liyajie
     * @date: 2021/8/30 0:14
     * @param studentId
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseTypeEntity> listAllCorusesByStudentId(String studentId);
    
    /**
     * 学习日程展示 
     * @author: liyajie 
     * @date: 2022/7/7 7:59
     * @param studentId
     * @return java.util.List<io.train.modules.business.common.coursetype.entity.CourseTypeEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<ClassesEntity> showFullCalendar(String studentId);
}

