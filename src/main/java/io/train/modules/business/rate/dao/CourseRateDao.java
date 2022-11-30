package io.train.modules.business.rate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.train.modules.business.rate.entity.CourseRateEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程评分接口
 * @author liyajie
 * @email sunlightcs@gmail.com
 * @date 2021-09-07 22:47:05
 */
@Mapper
public interface CourseRateDao extends BaseMapper<CourseRateEntity> {
	
}
