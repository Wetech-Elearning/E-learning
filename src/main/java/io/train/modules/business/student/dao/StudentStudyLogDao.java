package io.train.modules.business.student.dao;

import io.train.modules.business.student.entity.StudentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:44:03
 */
@Mapper
public interface StudentStudyLogDao extends BaseMapper<StudentEntity> {
	
	List<StudentEntity> getUserListByClassId(String uuid);
	
}
