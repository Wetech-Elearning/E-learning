package io.train.modules.business.record.dao;

import io.train.modules.business.record.entity.StudentStudyRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:43:20
 */
@Mapper
public interface StudentStudyRecordDao extends BaseMapper<StudentStudyRecordEntity> {
	
	public void asyncStudyRecord(StudentStudyRecordEntity studentStudyRecord);
}
