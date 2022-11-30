package io.train.modules.business.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.modules.business.course.entity.CourseRecord;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:39:34
 */
public interface CourseRecourdService extends IService<CourseRecord> {

    List<CourseRecord> queryList(Map<String, Object> params);
    
}

