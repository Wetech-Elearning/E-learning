package io.train.modules.business.coursereport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.coursereport.entity.CourseReportUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-08-17 17:16:04
 */
public interface CourseReportUserService extends IService<CourseReportUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CourseReportUserEntity> getByStudentId(String studentId);
}

