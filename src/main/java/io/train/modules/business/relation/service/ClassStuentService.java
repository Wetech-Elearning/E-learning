package io.train.modules.business.relation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.relation.entity.ClassStudentEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:48:33
 */
public interface ClassStuentService extends IService<ClassStudentEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据学员id查询所在班级 
     * @author: liyajie 
     * @date: 2022/6/30 14:43
     * @param studentId
     * @return java.util.List<io.train.modules.business.common.relation.entity.ClassStuentEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<ClassStudentEntity> getClassStuentEntityByStudentId(Long studentId);

    /**
     * 根据班级id查询班级所有学员 
     * @author: liyajie 
     * @date: 2022/8/17 17:33
     * @param classId
     * @return java.util.List<io.train.modules.business.common.relation.entity.ClassStudentEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<ClassStudentEntity> getClassStuentEntityByClassId(Long classId);
}

