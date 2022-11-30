package io.train.modules.business.rate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.modules.business.rate.entity.CourseRateEntity;

import java.util.List;

/**
 * 课程评分接口
 * @author liyajie
 * @email sunlightcs@gmail.com
 * @date 2021-09-07 22:47:05
 */
public interface CourseRateService extends IService<CourseRateEntity> {
    /**
     * 根据课程id查询评分
     * @author: liyajie
     * @date: 2021/9/7 22:57
     * @param courseId
     * @return java.util.List<io.train.modules.business.common.rate.entity.CourseRateEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CourseRateEntity> getRateByCourseId(Long courseId);

    /**
     * 保存评分
     * @author: liyajie
     * @date: 2021/9/7 23:12
     * @param courseRate
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void saveRate(CourseRateEntity courseRate);
}

