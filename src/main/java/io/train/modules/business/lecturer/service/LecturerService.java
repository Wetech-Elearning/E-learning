package io.train.modules.business.lecturer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.lecturer.entity.LecturerEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:41:24
 */
public interface LecturerService extends IService<LecturerEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据名称模糊查询讲师
     * @author: liyajie
     * @date: 2021/8/29 22:43
     * @param lecturerName
     * @return java.util.List<io.train.modules.business.common.lecturer.entity.LecturerEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<LecturerEntity> getLecturerLikeName(String lecturerName);

    /**
     * 查询所有的讲师
     * @author: liyajie
     * @date: 2022/1/24 17:30
     * @param lecturerName
     * @return java.util.List<io.train.modules.business.common.lecturer.entity.LecturerEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<LecturerEntity> getAllLecturer(String lecturerName);

    /**
     * 根据账号查询讲师
     * @author: liyajie
     * @date: 2022/1/24 17:30
     * @param account
     * @return io.train.modules.business.common.lecturer.entity.LecturerEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    LecturerEntity getByAccount(String account);

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
     * 导出讲师列表
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

