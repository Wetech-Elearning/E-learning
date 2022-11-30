package io.train.modules.business.course.service.impl;

import io.train.modules.business.course.dao.CourserRecordDao;
import io.train.modules.business.course.entity.CourseRecord;
import io.train.modules.business.course.service.CourseRecourdService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("courseRecourdServiceImpl")
public class CourseRecourdServiceImpl extends ServiceImpl<CourserRecordDao, CourseRecord> implements CourseRecourdService {

	@Override
	public List<CourseRecord> queryList(Map<String, Object> params) {
		return this.list(new QueryWrapper<CourseRecord>());
	}
}