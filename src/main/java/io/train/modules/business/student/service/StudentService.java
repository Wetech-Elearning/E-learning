package io.train.modules.business.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.student.entity.StudentEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:44:03
 */
public interface StudentService extends IService<StudentEntity> {

    PageUtils queryPage(Map<String, Object> params);


    PageUtils queryByClassPage(Map<String, Object> params);

    /**
     * 根据所属班级查询学员
     * @author: liyajie
     * @date: 2021/8/29 22:20
     * @param classUuid
     * @return java.util.List<io.train.modules.business.common.student.entity.StudentEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<StudentEntity> getStudentListByClassId(Long classUuid);

    /**
     * 根据所属科室查询学员
     * @author: liyajie
     * @date: 2021/8/29 22:20
     * @param officeId
     * @return java.util.List<io.train.modules.business.common.student.entity.StudentEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<StudentEntity> getStudentByRelateOffice(Long officeId);

    /**
     * 根据名称模糊查询学员
     * @author: liyajie
     * @date: 2021/8/29 22:36
     * @param studentName
     * @return java.util.List<io.train.modules.business.common.student.entity.StudentEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<StudentEntity> getStudentLikeName(String  studentName);

    /**
     * 查询所有的学生
     * @author: liyajie
     * @date: 2022/1/24 17:35
     * @param studentName
     * @return java.util.List<io.train.modules.business.common.student.entity.StudentEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<StudentEntity> getAllStudent(String studentName);

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
     * 导出学员列表
     * @author: liyajie
     * @date: 2022/7/1 17:56
     * @param response
     * @param fileName
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void exportExcel(HttpServletResponse response, String fileName);
}

